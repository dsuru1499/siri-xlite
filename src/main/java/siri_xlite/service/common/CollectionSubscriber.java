package siri_xlite.service.common;

import com.fasterxml.jackson.core.JsonGenerator;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Subscription;
import siri_xlite.Configuration;
import siri_xlite.common.HttpStatus;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public abstract class CollectionSubscriber<P extends DefaultParameters>
        implements SiriSubscriber<Document, P>, HttpStatus {

    protected final RoutingContext context;
    protected Configuration configuration;
    protected P parameters;
    protected ByteArrayOutputStream out;
    protected JsonGenerator writer;
    protected AtomicInteger count = new AtomicInteger();

    protected CollectionSubscriber(RoutingContext context) {
        this.context = context;
        this.out = new ByteArrayOutputStream();
        this.writer = createJsonWriter(out);
    }

    @Override
    public void configure(Configuration configuration, P parameters) {
        this.configuration = configuration;
        this.parameters = parameters;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Document t) {
        try {
            count.incrementAndGet();
            if (count.get() == 1) {
                writeStartDocument(writer, context.request().absoluteURI(), configuration.getVersion());
                writer.writeStartArray();
            }
            writeItem(t);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writeStartDocument(writer, context.request().absoluteURI(), configuration.getVersion());
                writer.writeStartArray();
            }
            writer.writeEndArray();
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
                SiriExceptionMarshaller.getInstance().write(writer, e);
            }
        } finally {
            this.context.response().setStatusCode(BAD_REQUEST);
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL, "max-age=30").end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    protected abstract void writeItem(Document t);

}