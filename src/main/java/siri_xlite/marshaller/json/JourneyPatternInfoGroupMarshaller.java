package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.VehicleModesEnumeration;

import java.util.List;
import java.util.stream.Collectors;

public class JourneyPatternInfoGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new JourneyPatternInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set journeyPatternRef
        writeField(writer, "JourneyPatternRef", source.getString("journeyPatternRef"));

        // set journeyPatternName
        writeField(writer, "JourneyPatternName", source.getString("journeyPatternName"));

        // set vehicleMode
        List<Integer> list = source.get("vehicleModes", List.class);
        String text = list.stream().map(t -> VehicleModesEnumeration.values()[t].name())
                .collect(Collectors.joining(","));
        writeField(writer, "VehicleMode", text);

        // set routeRef
        writeField(writer, "RouteRef", source.getString("routeRef"));

        // set publishedLineName
        writeField(writer, "PublishedLineName", source.getString("publishedLineName"));

        // groupOfLinesRef :string;

        // set directionName
        writeField(writer, "DirectionName", source.getString("directionName"));

        // externalLineRef :string;
    }

}
