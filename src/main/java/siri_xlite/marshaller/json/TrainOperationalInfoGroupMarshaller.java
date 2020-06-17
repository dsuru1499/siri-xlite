package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class TrainOperationalInfoGroupMarshaller implements Marshaller<Document> {

    private static final String TRAIN_NUMBERS = "trainNumbers";
    private static final String JOURNEY_PARTS = "journeyParts";
    private static final String JOURNEY_PART_REF = "journeyPartRef";
    private static final String TRAIN_NUMBER_REF = "trainNumberRef";
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

        List<String> trainNumbers = source.get(TRAIN_NUMBERS, List.class);
        writeArray(writer, TRAIN_NUMBERS, trainNumbers, t -> writeObject(writer, t, trainNumber -> writeField(writer, TRAIN_NUMBER_REF, trainNumber)));

        // set journeyParts

        List<Document> journeyParts = source.get(JOURNEY_PARTS, List.class);
        writeArray(writer, JOURNEY_PARTS, journeyParts, t -> writeObject(writer, t, journeyPart -> {
            writeField(writer, JOURNEY_PART_REF, journeyPart.getString(JOURNEY_PART_REF));
            writeField(writer, TRAIN_NUMBER_REF, journeyPart.getString(TRAIN_NUMBER_REF));

        }));

    }
}