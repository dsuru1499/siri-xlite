package siri_xlite.model;

import org.bson.Document;

import java.util.Map;

@org.springframework.data.mongodb.core.mapping.Document("stoppoints")
public class StopPointDocument extends Document {

    public StopPointDocument() {
        super();
    }

    public StopPointDocument(String key, Object value) {
        super(key, value);
    }

    public StopPointDocument(Map<String, Object> map) {
        super(map);
    }
}
