package siri_xlite.model;

import org.bson.Document;

import java.util.Map;

@org.springframework.data.mongodb.core.mapping.Document("vehicle_journey")

public class VehicleJourneyDocument extends Document {

    public VehicleJourneyDocument() {
        super();
    }

    public VehicleJourneyDocument(String key, Object value) {
        super(key, value);
    }

    public VehicleJourneyDocument(Map<String, Object> map) {
        super(map);
    }

    public VehicleJourneyDocument(Document document) {
        super(document);
    }
}
