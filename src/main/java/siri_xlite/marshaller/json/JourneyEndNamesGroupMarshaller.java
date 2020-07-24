package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

import static siri_xlite.common.JsonUtils.writeArray;
import static siri_xlite.common.JsonUtils.writeStringField;

public class JourneyEndNamesGroupMarshaller implements Marshaller<Document> {

    public static final String DESTINATION_REF = "destinationRef";
    public static final String ORIGIN_REF = "originRef";
    public static final String ORIGIN_NAME = "originName";
    public static final String VIAS = "vias";
    public static final String PLACE_REF = "placeRef";
    public static final String PLACE_NAME = "placeName";
    public static final String DESTINATION_NAME = "destinationName";

    @Getter
    private static final Marshaller<Document> instance = new JourneyEndNamesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set originRef
        writeStringField(writer, ORIGIN_REF, source);

        // set originName
        writeStringField(writer, ORIGIN_NAME, source);

        // originShortName :string;
        // destinationDisplayAtOrigin :string;

        // set via

        writeArray(writer, VIAS, source.get(VIAS, List.class), (Document t) -> {

            // set placeRef
            writeStringField(writer, PLACE_REF, t);

            // set placeName
            writeStringField(writer, PLACE_NAME, t);
        });

        // set destinationRef
        writeStringField(writer, DESTINATION_REF, source);

        // set destinationName
        writeStringField(writer, DESTINATION_NAME, source);

        // destinationShortName :string;
        // ? originDisplayAtDestination :string;

    }
}
