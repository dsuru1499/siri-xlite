package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

public class JourneyEndTimesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new JourneyEndTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set headwayService
        writeField(writer, "HeadwayService", source.getBoolean("headwayService"));

        // set originAimedDepartureTime
        writeField(writer, "OriginAimedDepartureTime", source.getDate("originAimedDepartureTime"));

        // set destinationAimedArrivalTime
        writeField(writer, "DestinationAimedArrivalTime", source.getDate("destinationAimedArrivalTime"));

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.getInteger("firstOrLastJourney");
        writeField(writer, "FirstOrLastJourney", FirstOrLastJourneyEnumeration.values()[firstOrLastJourney].name());

    }
}