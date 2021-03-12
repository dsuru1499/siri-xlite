package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.*;

public class TimetableAlterationGroupMarshaller implements Marshaller<Document> {

    public static final String EXTRA_JOURNEY = "extraJourney";
    public static final String CANCELLATION = "cancellation";
    public static final String DATED_VEHICLE_JOURNEY_REF = "datedVehicleJourneytRef";
    public static final String FRAMED_VEHICLE_JOURNEY_REF = "framedVehicleJourneyRef";
    public static final String DATA_FRAME_REF = "dataFrameRef";
    public static final String ANY = "any";

    @Getter
    private static final Marshaller<Document> instance = new TimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // FramedVehicleJourneyRef
        writeObject(writer, FRAMED_VEHICLE_JOURNEY_REF, source.getString(DATED_VEHICLE_JOURNEY_REF), datedVehicleJourneyRef -> {
            writeField(writer, DATED_VEHICLE_JOURNEY_REF, datedVehicleJourneyRef);
            writeField(writer, DATA_FRAME_REF, ANY);
        });

        // vehicleJourneyRef :string;

        // set extraJourney
        writeBooleanField(writer, EXTRA_JOURNEY, source);

        // set cancellation
        writeBooleanField(writer, CANCELLATION, source);

    }

}
