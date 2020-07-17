package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

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
        writeField(writer, ORIGIN_REF, source.getString(ORIGIN_REF));

        // set originName
        writeField(writer, ORIGIN_NAME, source.getString(ORIGIN_NAME));

        // originShortName :string;
        // destinationDisplayAtOrigin :string;

        // set via

        writeArray(writer, VIAS, source.get(VIAS, List.class), (Document t) -> {

            // set placeRef
            writeField(writer, PLACE_REF, t.getString(PLACE_REF));

            // set placeName
            writeField(writer, PLACE_NAME, t.getString(PLACE_NAME));
        });

        // set destinationRef
        writeField(writer, DESTINATION_REF, source.getString(DESTINATION_REF));

        // set destinationName
        writeField(writer, DESTINATION_REF, source.getString(DESTINATION_NAME));

        // destinationShortName :string;
        // ? originDisplayAtDestination :string;

    }
}
