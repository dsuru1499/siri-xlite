package siri_xlite.service.lines_discovery;

import com.fasterxml.jackson.core.JsonGenerator;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import siri_xlite.Configuration;
import siri_xlite.common.HttpStatus;
import siri_xlite.service.common.SiriException;
import siri_xlite.service.common.SiriSubscriber;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LinesDiscoverySubscriber implements SiriSubscriber<Document, LinesDiscoveryParameters>, HttpStatus {

    private Configuration configuration;
    private LinesDiscoveryParameters parameters;
    private final RoutingContext context;
    private ByteArrayOutputStream out;
    private JsonGenerator writer;
    private AtomicInteger count = new AtomicInteger();

    private LinesDiscoverySubscriber(RoutingContext context) {
        this.context = context;
        this.out = new ByteArrayOutputStream();
        this.writer = createJsonWriter(out);
    }

    public static SiriSubscriber<Document, LinesDiscoveryParameters> create(RoutingContext context) {
        return new LinesDiscoverySubscriber(context);
    }

    @Override
    public void configure(Configuration configuration, LinesDiscoveryParameters parameters) {
        this.configuration = configuration;
        this.parameters = parameters;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        writeStartDocument(writer);
    }

    @Override
    public void onNext(Document t) {
        try {
            count.incrementAndGet();
            if (count.get() == 1) {
                writer.writeObjectFieldStart("LinesDelivery");
                writer.writeArrayFieldStart("AnnotatedLineRef");
            }
            writeAnnotatedLineRef(t);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writer.writeObjectFieldStart("LinesDelivery");
                writer.writeArrayFieldStart("AnnotatedLineRef");
            }
            writer.writeEndArray();
            writer.writeEndObject();
            writeEndDocument(writer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        try {
            if (t instanceof SiriException) {
                SiriException e = (SiriException) t;

                writeObject(writer, "LinesDelivery", e, value -> {
                    writeField(writer, "ResponseTimestamp", parameters.getNow());
                    writeField(writer, "Status", false);
                    writeObject(writer, "ErrorCondition", value,
                            exception -> wrapper(() -> SiriExceptionMarshaller.getInstance().write(writer, exception)));
                });
            }
            writeEndDocument(writer);
        } finally {
            this.context.response().setStatusCode(BAD_REQUEST);
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL, "max-age=30").end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    private void writeAnnotatedLineRef(Document t) {
        writeObject(writer, t, source -> {
            writeField(writer, "LineRef", source.getString("lineRef"));
            writeField(writer, "LineName", source.getString("lineName"));
            writeField(writer, "Monitored", true);
            writeObject(writer, "Destinations", source.get("destinations", List.class),
                    (List<Document> destinations) -> writeArray(writer, "Destination", destinations,
                            this::writeDestination));
        });
    }

    private void writeDestination(Document source) {
        writeObject(writer, source, destination -> {
            writeField(writer, "DestinationRef", destination.getString("destinationRef"));
            writeField(writer, "PlaceName", destination.getString("placeName"));
        });
    }

}