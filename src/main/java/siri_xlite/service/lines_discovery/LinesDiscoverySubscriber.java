package siri_xlite.service.lines_discovery;

import com.fasterxml.jackson.core.JsonGenerator;
import io.reactivex.exceptions.Exceptions;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.reactivestreams.Subscription;
import siri_xlite.common.HttpStatus;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;
import siri_xlite.service.common.SiriException;
import siri_xlite.service.common.SiriStructureFactory;
import siri_xlite.service.common.SiriSubscriber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LinesDiscoverySubscriber implements SiriSubscriber<Document, LinesDiscoveryParameters>, HttpStatus {
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
    public void configure(LinesDiscoveryParameters parameters) {
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
                write(writer, "LinesDelivery", () -> {
                    write(writer, "ResponseTimestamp",
                            SiriStructureFactory.createXMLGregorianCalendar(parameters.getNow()));
                    write(writer, "Status", false);
                    write(writer, "ErrorCondition",
                            wrapper(() -> SiriExceptionMarshaller.getInstance().write(writer, e)));
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
                this.context.response().putHeader("Content-Type", "application/json").end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    private void writeAnnotatedLineRef(Document document) throws IOException {
        write(writer, () -> {
            write(writer, "LineRef", document.getString("lineRef"));
            write(writer, "LineName", document.getString("lineName"));
            write(writer, "Monitored", true);

            List<Document> list = (List<Document>) document.get("destinations");
            if (CollectionUtils.isNotEmpty(list)) {
                write(writer, "Destinations", () -> {
                    write(writer, "Destination", list, (t) -> {
                        write(writer, () -> {
                            write(writer, "DestinationRef", t.getString("destinationRef"));
                            write(writer, "PlaceName", t.getString("placeName"));
                        });
                    });
                });
            }
        });
    }

}