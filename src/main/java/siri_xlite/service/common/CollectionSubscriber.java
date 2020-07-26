package siri_xlite.service.common;

import io.reactivex.exceptions.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.Date;

import static siri_xlite.common.JsonUtils.writeEndDocument;
import static siri_xlite.common.JsonUtils.writeStartDocument;

@Slf4j
public abstract class CollectionSubscriber<P extends DefaultParameters> extends SiriSubscriber<Document, P> {

    @Override
    public void onNext(Document document) {
        try {
            count.incrementAndGet();
            if (count.get() == 1) {
                writeStartDocument(writer, configuration);
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
                writeNotFound();
            } else {
                writer.writeEndArray();
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