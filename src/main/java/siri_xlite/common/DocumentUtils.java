package siri_xlite.common;

import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;

import java.util.Collection;

public interface DocumentUtils {

    static Document append(Document document, String key, Object value) {
        if (value != null) {
            document.append(key, value);
        }
        return document;
    }

    static Document append(Document document, String key, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            document.append(key, values);
        }

        return document;
    }
}
