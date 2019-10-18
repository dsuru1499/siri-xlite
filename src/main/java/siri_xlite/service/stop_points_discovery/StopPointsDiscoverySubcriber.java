package siri_xlite.service.stop_points_discovery;

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
public class StopPointsDiscoverySubcriber
        implements SiriSubscriber<Document, StopPointsDiscoveryParameters>, HttpStatus {

    private Configuration configuration;
    private StopPointsDiscoveryParameters parameters;
    private final RoutingContext context;
    private ByteArrayOutputStream out;
    private JsonGenerator writer;
    private AtomicInteger count = new AtomicInteger();

    public StopPointsDiscoverySubcriber(RoutingContext context) {
        this.context = context;
        this.out = new ByteArrayOutputStream();
        this.writer = createJsonWriter(out);
    }

    public static SiriSubscriber<Document, StopPointsDiscoveryParameters> create(RoutingContext context) {
        return new StopPointsDiscoverySubcriber(context);
    }

    @Override
    public void configure(Configuration configuration, StopPointsDiscoveryParameters parameters) {
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
                writer.writeObjectFieldStart("StopPointsDelivery");
                writer.writeArrayFieldStart("AnnotatedStopPointRef");
            }
            writeAnnotatedStopPointRef(t);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writer.writeObjectFieldStart("StopPointsDelivery");
                writer.writeArrayFieldStart("AnnotatedStopPointRef");
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

                writeObject(writer, "StopPointsDelivery", e, value -> {
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

    private void writeAnnotatedStopPointRef(Document t) {
        writeObject(writer, t, source -> {
            writeField(writer, "StopPointRef", source.getString("stopPointRef"));
            writeField(writer, "stopName", source.getString("stopName"));
            writeArray(writer, "Lines", (List<String>) source.get("lineRefs"),
                    value -> writeObject(writer, value, (lineRef) -> writeField(writer, "LineRef", lineRef)));
            writeObject(writer, "Location", (Document) source.get("location"), (Document location) -> {
                writeField(writer, "Longitude", location.getDouble("longitude"));
                writeField(writer, "Latitude", location.getDouble("latitude"));
            });
        });
    }

}