package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.DepartureBoardingActivityEnumeration;

public class AimedCallGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new AimedCallGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, "AimedArrivalTime", source.getDate("aimedArrivalTime"));

        // set arrivalPlatformName
        writeField(writer, "AimedDepartureTime", source.getString("arrivalPlatformName"));

        // arrivalBoardingActivity :byte;

        // set arrivalOperatorRefs

        // set aimedDepartureTime
        writeField(writer, "AimedDepartureTime", source.getDate("aimedDepartureTime"));

        // set departurePlatformName
        writeField(writer, "DeparturePlatformName", source.getString("departurePlatformName"));

        // set departureBoardingActivity
        int departureBoardingActivity = source.getInteger("departureBoardingActivity");
        writeField(writer, "DepartureBoardingActivity",
                DepartureBoardingActivityEnumeration.values()[departureBoardingActivity].name());

        // set departureOperatorRefs

        // aimedLatestPassengerAccessTime :long;

        // set aimedHeadwayInterval
        long aimedHeadwayInterval = source.getLong("aimedHeadwayInterval");
        writeField(writer, "AimedHeadwayInterval", SiriStructureFactory.createDuration(aimedHeadwayInterval));

    }

}
