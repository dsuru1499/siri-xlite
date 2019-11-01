package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class JourneyInfoGroupMarshaller implements Marshaller<Document> {

    public static final String VEHICLE_JOURNEY_NAME = "vehicleJourneyName";
    public static final String JOURNEY_NOTE = "journeyNote";
    @Getter
    private static final Marshaller<Document> instance = new JourneyInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set vehicleJourneyName
        writeField(writer, VEHICLE_JOURNEY_NAME, source.getString(VEHICLE_JOURNEY_NAME));

        // set journeyNote
        writeArray(writer, JOURNEY_NOTE, source.get(JOURNEY_NOTE, List.class));

        // publicContact :SimpleContact;
        // operationsContact:SimpleContact;
    }

}
