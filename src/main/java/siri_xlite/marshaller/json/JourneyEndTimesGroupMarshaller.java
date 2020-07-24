package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

import static siri_xlite.common.JsonUtils.*;

public class JourneyEndTimesGroupMarshaller implements Marshaller<Document> {

    public static final String ORIGIN_AIMED_DEPARTURE_TIME = "originAimedDepartureTime";
    public static final String HEADWAY_SERVICE = "headwayService";
    public static final String DESTINATION_AIMED_ARRIVAL_TIME = "destinationAimedArrivalTime";
    public static final String FIRST_OR_LAST_JOURNEY = "firstOrLastJourney";

    @Getter
    private static final Marshaller<Document> instance = new JourneyEndTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set headwayService
        writeBooleanField(writer, HEADWAY_SERVICE, source);

        // set originAimedDepartureTime
        writeLocalTimeField(writer, ORIGIN_AIMED_DEPARTURE_TIME, source);

        // set destinationAimedArrivalTime
        writeLocalTimeField(writer, DESTINATION_AIMED_ARRIVAL_TIME, source);

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.getInteger(FIRST_OR_LAST_JOURNEY);
        if (firstOrLastJourney != null) {
            writeField(writer, FIRST_OR_LAST_JOURNEY, FirstOrLastJourneyEnumeration.values()[firstOrLastJourney]);
        }

    }
}