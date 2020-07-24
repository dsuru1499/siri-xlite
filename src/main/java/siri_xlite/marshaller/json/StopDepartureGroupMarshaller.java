package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri_xlite.common.JsonUtils.*;

public class StopDepartureGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String ACTUAL_DEPARTURE_TIME = "actualDepartureTime";
    public static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";
    public static final String DEPARTURE_STATUS = "departureStatus";
    public static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";

    @Getter
    private static final Marshaller<Document> instance = new StopDepartureGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeLocalTimeField(writer, AIMED_DEPARTURE_TIME, source);

        // set actualDepartureTime
        writeLocalTimeField(writer, ACTUAL_DEPARTURE_TIME, source);

        // set expectedDepartureTime
        writeLocalTimeField(writer, EXPECTED_DEPARTURE_TIME, source);

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;

        // aimedLatestPassengerAccessTime :long;

        // expectedLatestPassengerAccessTime :long;

        // set departureStatus
        Integer departureStatus = source.getInteger(DEPARTURE_STATUS);
        if (departureStatus != null) {
            writeField(writer, DEPARTURE_STATUS, CallStatusEnumeration.values()[departureStatus]);
        }

        // departureProximityText :string;

        // set departurePlatformName
        writeStringField(writer, DEPARTURE_PLATFORM_NAME, source);

        // departureBoardingActivity :byte;

        // departureStopAssignment :StopAssignment;

        // departureOperatorRefs

    }
}