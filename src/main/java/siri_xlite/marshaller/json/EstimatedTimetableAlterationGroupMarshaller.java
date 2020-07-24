package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeBooleanField;
import static siri_xlite.common.JsonUtils.writeStringField;

public class EstimatedTimetableAlterationGroupMarshaller implements Marshaller<Document> {

    public static final String DATED_VEHICLE_JOURNEY_REF = "datedVehicleJourneyRef";
    public static final String EXTRA_JOURNEY = "extraJourney";
    public static final String CANCELLATION = "cancellation";

    @Getter
    private static final Marshaller<Document> instance = new EstimatedTimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set datedVehicleJourneyRef
        writeStringField(writer, DATED_VEHICLE_JOURNEY_REF, source);

        // datedVehicleJourneyIndirectRef :string;
        // estimatedVehicleJourneyCode :string;

        // set extraJourney
        writeBooleanField(writer, EXTRA_JOURNEY, source);

        // set cancellation
        writeStringField(writer, CANCELLATION, source);

    }

}