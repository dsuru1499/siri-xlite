package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;

public class EstimatedTimetableAlterationGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new EstimatedTimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set datedVehicleJourneyRef
        String datedVehicleJourneyRef = source.datedVehicleJourneyRef();
        if (datedVehicleJourneyRef != null && !datedVehicleJourneyRef.isEmpty()) {
            writer.writeStringField("DatedVehicleJourneyRef", datedVehicleJourneyRef);
        }

        // datedVehicleJourneyIndirectRef :string;
        // estimatedVehicleJourneyCode :string;

        // set extraJourney
        boolean extraJourney = source.extraJourney();
        writer.writeBooleanField("ExtraJourney", extraJourney);

        // set cancellation
        boolean cancellation = source.cancellation();
        writer.writeBooleanField("Cancellation", cancellation);

    }

}