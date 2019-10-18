package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class TrainOperationalInfoGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new TrainOperationalInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // trainBlockPart : [TrainBlockPart];
        // blockRef :string;
        // courseOfJourneyRef :string;
        // DocumentRef :string;
        // vehicleRef :string;
        // additionalDocumentRef :[string];
        // driverRef :string;
        // driverName :string;

        // set trainNumbers
        List<String> trainNumbers = source.get("trainNumbers", List.class);
        writeArray(writer, "TrainNumbers", trainNumbers, t -> {
            writeObject(writer, t, trainNumber -> writeField(writer, "TrainNumberRef", trainNumber));
        });

        // set journeyParts
        List<Document> journeyParts = source.get("journeyParts", List.class);
        writeArray(writer, "JourneyParts", journeyParts, t -> {
            writeObject(writer, t, journeyPart -> {
                writeField(writer, "JourneyPartRef", journeyPart.getString("journeyPartRef"));
                writeField(writer, "TrainNumberRef", journeyPart.getString("trainNumberRef"));

            });
        });

    }
}