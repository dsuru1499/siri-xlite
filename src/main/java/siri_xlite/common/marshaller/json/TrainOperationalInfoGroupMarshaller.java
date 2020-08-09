package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

import static siri_xlite.common.JsonUtils.*;

public class TrainOperationalInfoGroupMarshaller implements Marshaller<Document> {

    public static final String TRAIN_NUMBERS = "trainNumbers";
    public static final String JOURNEY_PARTS = "journeyParts";
    public static final String JOURNEY_PART_REF = "journeyPartRef";
    public static final String TRAIN_NUMBER_REF = "trainNumberRef";

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

        List<?> trainNumbers = source.get(TRAIN_NUMBERS, List.class);
        writeArray(writer, TRAIN_NUMBERS, trainNumbers,
                t -> writeObject(writer, t, trainNumber -> writeField(writer, TRAIN_NUMBER_REF, (String) trainNumber)));

        // set journeyParts
        List<?> journeyParts = source.get(JOURNEY_PARTS, List.class);
        writeArray(writer, JOURNEY_PARTS, journeyParts, t -> writeObject(writer, t, o -> {
            if (o instanceof Document) {
                Document journeyPart = (Document) o;
                writeStringField(writer, JOURNEY_PART_REF, journeyPart);
                writeStringField(writer, TRAIN_NUMBER_REF, journeyPart);
            }
        }));

    }
}