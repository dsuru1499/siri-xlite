package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import uk.org.siri.siri.VehicleModesEnumeration;

import java.util.List;
import java.util.stream.Collectors;

import static siri_xlite.common.JsonUtils.writeField;
import static siri_xlite.common.JsonUtils.writeStringField;

public class JourneyPatternInfoGroupMarshaller implements Marshaller<Document> {

    public static final String ROUTE_REF = "routeRef";
    public static final String JOURNEY_PATTERN_REF = "journeyPatternRef";
    public static final String JOURNEY_PATTERN_NAME = "journeyPatternName";
    public static final String VEHICLE_MODES = "vehicleModes";
    public static final String PUBLISHED_LINE_NAME = "publishedLineName";
    public static final String DIRECTION_NAME = "directionName";

    @Getter
    private static final Marshaller<Document> instance = new JourneyPatternInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set journeyPatternRef
        writeStringField(writer, JOURNEY_PATTERN_REF, source);

        // set journeyPatternName
        writeStringField(writer, JOURNEY_PATTERN_NAME, source);

        // set vehicleMode

        List<Integer> values = source.get(VEHICLE_MODES, List.class);
        if (CollectionUtils.isNotEmpty(values)) {
            String text = values.stream().map(t -> VehicleModesEnumeration.values()[t].name())
                    .collect(Collectors.joining(","));
            writeField(writer, VEHICLE_MODES, text);
        }

        // set routeRef
        writeStringField(writer, ROUTE_REF, source);

        // set publishedLineName
        writeStringField(writer, PUBLISHED_LINE_NAME, source);

        // groupOfLinesRef :string;

        // set directionName
        writeStringField(writer, DIRECTION_NAME, source);

        // externalLineRef :string;
    }

}
