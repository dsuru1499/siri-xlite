package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class JourneyEndNamesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new JourneyEndNamesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set originRef
        writeField(writer, "OriginRef", source.getString("originRef"));

        // set originName
        writeField(writer, "OriginName", source.getString("originName"));

        // originShortName :string;
        // destinationDisplayAtOrigin :string;

        // set via
        writeArray(writer, "Via", source.get("vias", List.class), (Document t) -> {

            // set placeRef
            writeField(writer, "PlaceRef", t.getString("placeRef"));

            // set placeName
            writeField(writer, "PlaceName", t.getString("placeName"));
        });

        // set destinationRef
        writeField(writer, "DestinationRef", source.getString("destinationRef"));

        // set destinationName
        writeField(writer, "DestinationName", source.getString("destinationName"));

        // destinationShortName :string;
        // ? originDisplayAtDestination :string;

    }
}
