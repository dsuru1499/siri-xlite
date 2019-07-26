package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.VehicleJourney;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public class TimetableAlterationGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new TimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {
        // framedVehicleJourneyRef
        String datedVehicleJourneyRef = source.datedVehicleJourneyRef();
        if (datedVehicleJourneyRef != null && !datedVehicleJourneyRef.isEmpty()) {
            writer.writeObjectFieldStart("FramedVehicleJourneyRef");
            writer.writeStringField("DatedVehicleJourneyRef", datedVehicleJourneyRef);
            writer.writeStringField("DataFrameRef", SiriStructureFactory.createXMLGregorianCalendar());
            writer.writeEndObject();
        }

        // vehicleJourneyRef :string;

        // set extraJourney
        boolean extraJourney = source.extraJourney();
        writer.writeBooleanField("ExtraJourney", extraJourney);

        // set cancellation
        boolean cancellation = source.cancellation();
        writer.writeBooleanField("Cancellation", cancellation);
    }

}
