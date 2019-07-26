package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.VehicleJourney;
import uk.org.siri.siri.VehicleModesEnumeration;

import java.io.IOException;
import java.util.List;

public class JourneyPatternInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyPatternInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set journeyPatternRef
        String journeyPatternRef = source.journeyPatternRef();
        if (journeyPatternRef != null && !journeyPatternRef.isEmpty()) {
            writer.writeStringField("JourneyPatternRef", journeyPatternRef);
        }

        // set journeyPatternName
        String journeyPatternName = source.journeyPatternName();
        if (journeyPatternName != null && !journeyPatternName.isEmpty()) {
            writer.writeStringField("JourneyPatternName", journeyPatternName);
        }

        // set vehicleMode
        List<Integer> list = source.vehicleModes();
        if (list.size() > 0) {
            StringBuffer text = null;
            for (Integer value : list) {
                VehicleModesEnumeration vehicleMode = (VehicleModesEnumeration.values()[value]);
                if (text == null) {
                    text = new StringBuffer();
                    text.append(vehicleMode.name());
                } else {
                    text.append(',');
                    text.append(vehicleMode.name());
                }
            }
            writer.writeStringField("VehicleMode", text.toString());
        }

        // set routeRef
        String routeRef = source.routeRef();
        if (routeRef != null && !routeRef.isEmpty()) {
            writer.writeStringField("RouteRef", routeRef);
        }

        // set publishedLineName
        String publishedLineName = source.publishedLineName();
        if (publishedLineName != null && !publishedLineName.isEmpty()) {
            writer.writeStringField("PublishedLineName", publishedLineName);
        }

        // groupOfLinesRef :string;

        // set directionName
        String directionName = source.directionName();
        if (directionName != null && !directionName.isEmpty()) {
            writer.writeStringField("DirectionName", directionName);
        }

        // externalLineRef :string;
    }

}
