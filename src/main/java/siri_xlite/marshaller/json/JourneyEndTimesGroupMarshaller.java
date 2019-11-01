package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

public class JourneyEndTimesGroupMarshaller implements Marshaller<Document> {

    public static final String HEADWAY_SERVICE = "headwayService";
    public static final String ORIGIN_AIMED_DEPARTURE_TIME = "originAimedDepartureTime";
    public static final String DESTINATION_AIMED_ARRIVAL_TIME = "destinationAimedArrivalTime";
    public static final String FIRST_OR_LAST_JOURNEY = "firstOrLastJourney";
    @Getter
    private static final Marshaller<Document> instance = new JourneyEndTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set headwayService
        writeField(writer, HEADWAY_SERVICE, source.getBoolean(HEADWAY_SERVICE));

        // set originAimedDepartureTime
        writeField(writer, ORIGIN_AIMED_DEPARTURE_TIME, source.getDate(ORIGIN_AIMED_DEPARTURE_TIME));

        // set destinationAimedArrivalTime
        writeField(writer, DESTINATION_AIMED_ARRIVAL_TIME, source.getDate(DESTINATION_AIMED_ARRIVAL_TIME));

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.getInteger(FIRST_OR_LAST_JOURNEY);
        if (firstOrLastJourney != null) {
            writeField(writer, FIRST_OR_LAST_JOURNEY, FirstOrLastJourneyEnumeration.values()[firstOrLastJourney]);
        }

    }
}