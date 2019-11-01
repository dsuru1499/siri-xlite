package siri_xlite.model;

import org.bson.Document;

import java.util.Map;

@org.springframework.data.mongodb.core.mapping.Document("lines")
public class LineDocument extends Document {

    public LineDocument() {
    }

    public LineDocument(String key, Object value) {
        super(key, value);
    }

    public LineDocument(Map<String, Object> map) {
        super(map);
    }
}
