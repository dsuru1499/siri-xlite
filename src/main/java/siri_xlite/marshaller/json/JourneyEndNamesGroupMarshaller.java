package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.VehicleJourney;
import siri_xlite.model.Via;

import java.io.IOException;
import java.util.List;

public class JourneyEndNamesGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyEndNamesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set originRef
        String originRef = source.originRef();
        if (originRef != null && !originRef.isEmpty()) {
            writer.writeStringField("OriginRef", originRef);
        }

        // set originName
        String originName = source.originName();
        if (originName != null && !originName.isEmpty()) {
            writer.writeStringField("OriginName", originName);
        }

        // originShortName :string;
        // destinationDisplayAtOrigin :string;

        // set via
        List<Via> vias = source.vias();
        if (vias != null && vias.size() > 0) {

            writer.writeArrayFieldStart("Via");
            for (Via via : vias) {

                // set placeRef
                String placeRef = via.placeRef();
                if (placeRef != null && !placeRef.isEmpty()) {
                    writer.writeStringField("PlaceRef", placeRef);
                }

                // set placeName
                String placeName = via.placeName();
                if (placeName != null && !placeName.isEmpty()) {
                    writer.writeStringField("PlaceName", placeName);
                }
            }
            writer.writeEndArray();
        }

        // set destinationRef
        String destinationRef = source.destinationRef();
        if (destinationRef != null && !destinationRef.isEmpty()) {
            writer.writeStringField("DestinationRef", destinationRef);
        }

        // set destinationName
        String destinationName = source.destinationName();
        if (destinationName != null && !destinationName.isEmpty()) {
            writer.writeStringField("DestinationName", destinationName);
        }

        // destinationShortName :string;
        // ? originDisplayAtDestination :string;

    }
}
