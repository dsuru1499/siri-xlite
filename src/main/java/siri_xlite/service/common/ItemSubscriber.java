package siri_xlite.service.common;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class ItemSubscriber<P extends DefaultParameters> extends SiriSubscriber<Document, P> {

    private final AtomicInteger count = new AtomicInteger();
    private Document current;

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Document document) {
        try {
            count.incrementAndGet();
            this.current = document;
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
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL,
                                Arrays.asList(PUBLIC, MAX_AGE, S_MAX_AGE, PROXY_REVALIDATE))
                        .putHeader(HttpHeaders.ETAG, createEtag(current)).end(out.toString());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    public String getEtag() {
        return createEtag(current);
    }

    protected abstract void writeItem(Document t);

}