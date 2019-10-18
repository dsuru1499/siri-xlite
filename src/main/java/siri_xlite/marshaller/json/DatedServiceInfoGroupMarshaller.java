package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

public class DatedServiceInfoGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new DatedServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set destinationDisplay
        writeField(writer, "DestinationDisplay", source.getString("destinationDisplay"));

        // lineNote :[string];

        // set firstOrLastJourney
        int firstOrLastJourney = source.getInteger("firstOrLastJourney");
        writeField(writer, "FirstOrLastJourney", FirstOrLastJourneyEnumeration.values()[firstOrLastJourney].name());
    }

}
