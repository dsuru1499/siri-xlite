package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.DepartureBoardingActivityEnumeration;

public class AimedCallGroupMarshaller implements Marshaller<Document> {

    private static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    private static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";
    private static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    private static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";
    private static final String DEPARTURE_BOARDING_ACTIVITY = "departureBoardingActivity";
    private static final String AIMED_HEADWAY_INTERVAL = "aimedHeadwayInterval";
    @Getter
    private static final Marshaller<Document> instance = new AimedCallGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, source.getDate(AIMED_ARRIVAL_TIME));

        // set arrivalPlatformName
        writeField(writer, ARRIVAL_PLATFORM_NAME, source.getString(ARRIVAL_PLATFORM_NAME));

        // arrivalBoardingActivity :byte;

        // set arrivalOperatorRefs

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, source.getDate(AIMED_DEPARTURE_TIME));

        // set departurePlatformName
        writeField(writer, DEPARTURE_PLATFORM_NAME, source.getString(DEPARTURE_PLATFORM_NAME));

        // set departureBoardingActivity
        Integer departureBoardingActivity = source.getInteger(DEPARTURE_BOARDING_ACTIVITY);
        if (departureBoardingActivity != null) {
            writeField(writer, DEPARTURE_BOARDING_ACTIVITY,
                    DepartureBoardingActivityEnumeration.values()[departureBoardingActivity]);
        }
        // set departureOperatorRefs

        // aimedLatestPassengerAccessTime :long;

        // set aimedHeadwayInterval
        Long aimedHeadwayInterval = source.getLong(AIMED_HEADWAY_INTERVAL);
        writeField(writer, AIMED_HEADWAY_INTERVAL, SiriStructureFactory.createDuration(aimedHeadwayInterval));

    }

}
