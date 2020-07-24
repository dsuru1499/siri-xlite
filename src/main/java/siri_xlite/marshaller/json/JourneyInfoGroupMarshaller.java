package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeArrayField;
import static siri_xlite.common.JsonUtils.writeStringField;

public class JourneyInfoGroupMarshaller implements Marshaller<Document> {

    public static final String VEHICLE_JOURNEY_NAME = "vehicleJourneyName";
    public static final String JOURNEY_NOTE = "journeyNote";

    @Getter
    private static final Marshaller<Document> instance = new JourneyInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set vehicleJourneyName
        writeStringField(writer, VEHICLE_JOURNEY_NAME, source);

        // set journeyNote

        writeArrayField(writer, JOURNEY_NOTE, source);

        // publicContact :SimpleContact;
        // operationsContact:SimpleContact;
    }

}
