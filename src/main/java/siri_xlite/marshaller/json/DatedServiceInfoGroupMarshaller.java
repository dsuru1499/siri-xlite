package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.VehicleJourney;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

import java.io.IOException;

public class DatedServiceInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new DatedServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set destinationDisplay
        String destinationDisplay = source.destinationDisplay();
        if (destinationDisplay != null && !destinationDisplay.isEmpty()) {
            writer.writeStringField("DestinationDisplay", destinationDisplay);
        }

        // lineNote :[string];

        // set firstOrLastJourney
        int firstOrLastJourney = source.firstOrLastJourney();
        writer.writeStringField("FirstOrLastJourney",
                FirstOrLastJourneyEnumeration.values()[firstOrLastJourney].name());
    }

}
