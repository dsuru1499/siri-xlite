package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class JourneyInfoGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new JourneyInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set vehicleJourneyName
        writeField(writer, "VehicleJourneyName", source.getString("vehicleJourneyName"));

        // set journeyNote
        writeArray(writer, "", source.get("JourneyNote", List.class));

        // publicContact :SimpleContact;
        // operationsContact:SimpleContact;
    }

}
