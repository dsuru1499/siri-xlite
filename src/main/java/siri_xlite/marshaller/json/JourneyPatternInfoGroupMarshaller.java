package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import uk.org.siri.siri.VehicleModesEnumeration;

import java.util.List;
import java.util.stream.Collectors;

public class JourneyPatternInfoGroupMarshaller implements Marshaller<Document> {

    public static final String ROUTE_REF = "routeRef";
    private static final String JOURNEY_PATTERN_REF = "journeyPatternRef";
    private static final String JOURNEY_PATTERN_NAME = "journeyPatternName";
    private static final String VEHICLE_MODES = "vehicleModes";
    private static final String PUBLISHED_LINE_NAME = "publishedLineName";
    private static final String DIRECTION_NAME = "directionName";
    @Getter
    private static final Marshaller<Document> instance = new JourneyPatternInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set journeyPatternRef
        writeField(writer, JOURNEY_PATTERN_REF, source.getString(JOURNEY_PATTERN_REF));

        // set journeyPatternName
        writeField(writer, JOURNEY_PATTERN_NAME, source.getString(JOURNEY_PATTERN_NAME));

        // set vehicleMode

        List<Integer> values = source.get(VEHICLE_MODES, List.class);
        if (CollectionUtils.isNotEmpty(values)) {
            String text = values.stream().map(t -> VehicleModesEnumeration.values()[t].name())
                    .collect(Collectors.joining(","));
            writeField(writer, VEHICLE_MODES, text);
        }

        // set routeRef
        writeField(writer, ROUTE_REF, source.getString(ROUTE_REF));

        // set publishedLineName
        writeField(writer, PUBLISHED_LINE_NAME, source.getString(PUBLISHED_LINE_NAME));

        // groupOfLinesRef :string;

        // set directionName
        writeField(writer, DIRECTION_NAME, source.getString(DIRECTION_NAME));

        // externalLineRef :string;
    }

}
