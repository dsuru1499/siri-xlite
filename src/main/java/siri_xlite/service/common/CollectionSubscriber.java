package siri_xlite.service.common;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.http.MediaType;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;

import java.util.Arrays;
import java.util.Date;

@Slf4j
public abstract class CollectionSubscriber<P extends DefaultParameters> extends SiriSubscriber<Document, P> {

    @Override
    public void onNext(Document document) {
        try {
            count.incrementAndGet();
            if (count.get() == 1) {
                writeStartDocument(writer, context.request().absoluteURI(), configuration.getVersion());
                writer.writeStartArray();
            }
            if (current == null || CacheControl.COMPARATOR.compare(document, current) > 0) {
                this.current = document;
            }
            writeItem(document);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                SiriExceptionMarshaller.getInstance().write(writer, SiriException.createInvalidDataReferencesError());
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(out.toString());
            } else {
                writer.writeEndArray();
                writeEndDocument(writer);
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL, Arrays.asList(
                                CacheControl.MAX_AGE + parameters.getMaxAge(),
                                CacheControl.S_MAX_AGE + parameters.getSMaxAge()))
                        .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(getLastModified()))
                        .end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    public Date getLastModified() {
        return CacheControl.getLastModified(current);
    }

    protected abstract void writeItem(Document t);

}