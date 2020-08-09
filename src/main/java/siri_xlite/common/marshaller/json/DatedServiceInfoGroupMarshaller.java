package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

import static siri_xlite.common.JsonUtils.writeField;
import static siri_xlite.common.JsonUtils.writeStringField;

public class DatedServiceInfoGroupMarshaller implements Marshaller<Document> {

    public static final String DESTINATION_DISPLAY = "destinationDisplay";
    public static final String FIRST_OR_LAST_JOURNEY = "firstOrLastJourney";

    @Getter
    private static final Marshaller<Document> instance = new DatedServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set destinationDisplay
        writeStringField(writer, DESTINATION_DISPLAY, source);

        // lineNote :[string];

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.getInteger(FIRST_OR_LAST_JOURNEY);
        if (firstOrLastJourney != null) {
            writeField(writer, FIRST_OR_LAST_JOURNEY, FirstOrLastJourneyEnumeration.values()[firstOrLastJourney]);
        }
    }

}
