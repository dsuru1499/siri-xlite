package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.DepartureBoardingActivityEnumeration;

import static siri_xlite.common.JsonUtils.*;

public class AimedCallGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";
    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";
    public static final String DEPARTURE_BOARDING_ACTIVITY = "departureBoardingActivity";
    public static final String AIMED_HEADWAY_INTERVAL = "aimedHeadwayInterval";

    @Getter
    private static final Marshaller<Document> instance = new AimedCallGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeLocalTimeField(writer, AIMED_ARRIVAL_TIME, source);

        // set arrivalPlatformName
        writeLocalTimeField(writer, ARRIVAL_PLATFORM_NAME, source);

        // arrivalBoardingActivity :byte;

        // set arrivalOperatorRefs

        // set aimedDepartureTime
        writeLocalTimeField(writer, AIMED_DEPARTURE_TIME, source);

        // set departurePlatformName
        writeStringField(writer, DEPARTURE_PLATFORM_NAME, source);

        // set departureBoardingActivity
        Integer departureBoardingActivity = source.getInteger(DEPARTURE_BOARDING_ACTIVITY);
        if (departureBoardingActivity != null) {
            writeField(writer, DEPARTURE_BOARDING_ACTIVITY,
                    DepartureBoardingActivityEnumeration.values()[departureBoardingActivity]);
        }
        // set departureOperatorRefs

        // aimedLatestPassengerAccessTime :long;

        // set aimedHeadwayInterval
        writeDurationField(writer, AIMED_HEADWAY_INTERVAL, source);

    }

}
