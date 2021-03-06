package siri_xlite.common;

import io.reactivex.rxjava3.exceptions.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Subscription;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static siri_xlite.common.JsonUtils.writeEndDocument;
import static siri_xlite.common.JsonUtils.writeStartDocument;

@Slf4j
public abstract class ItemSubscriber<P extends DefaultParameters> extends SiriSubscriber<Document, P> {

    protected final AtomicInteger count = new AtomicInteger();
    protected Document current;

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Document document) {
        try {
            count.incrementAndGet();
            this.current = document;
            writeStartDocument(writer, parameters);
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
                writeNotFound();
            } else {
                writeEndDocument(writer);
                writeResponse(getLastModified());
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