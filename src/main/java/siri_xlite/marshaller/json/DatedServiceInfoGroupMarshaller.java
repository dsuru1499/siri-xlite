package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

public class DatedServiceInfoGroupMarshaller implements Marshaller<Document> {

    public static final String DESTINATION_DISPLAY = "destinationDisplay";
    public static final String FIRST_OR_LAST_JOURNEY = "firstOrLastJourney";
    @Getter
    private static final Marshaller<Document> instance = new DatedServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set destinationDisplay
        writeField(writer, DESTINATION_DISPLAY, source.getString(DESTINATION_DISPLAY));

        // lineNote :[string];

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.getInteger(FIRST_OR_LAST_JOURNEY);
        if (firstOrLastJourney != null) {
            writeField(writer, FIRST_OR_LAST_JOURNEY, FirstOrLastJourneyEnumeration.values()[firstOrLastJourney]);
        }
    }

}
