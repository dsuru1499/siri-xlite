package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class EstimatedTimetableAlterationGroupMarshaller implements Marshaller<Document> {

    public static final String DATED_VEHICLE_JOURNEY_REF = "datedVehicleJourneyRef";
    private static final String EXTRA_JOURNEY = "extraJourney";
    private static final String CANCELLATION = "cancellation";
    @Getter
    private static final Marshaller<Document> instance = new EstimatedTimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set datedVehicleJourneyRef
        writeField(writer, DATED_VEHICLE_JOURNEY_REF, source.getString(DATED_VEHICLE_JOURNEY_REF));

        // datedVehicleJourneyIndirectRef :string;
        // estimatedVehicleJourneyCode :string;

        // set extraJourney
        writeField(writer, EXTRA_JOURNEY, source.getBoolean(EXTRA_JOURNEY));

        // set cancellation
        writeField(writer, CANCELLATION, source.getString(CANCELLATION));

    }

}