package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri_xlite.common.DateTimeUtils.toLocalTime;

public class StopDepartureGroupMarshaller implements Marshaller<Document> {

    private static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    private static final String ACTUAL_DEPARTURE_TIME = "actualDepartureTime";
    private static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";
    private static final String DEPARTURE_STATUS = "departureStatus";
    private static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";
    @Getter
    private static final Marshaller<Document> instance = new StopDepartureGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, toLocalTime(source.getDate(AIMED_DEPARTURE_TIME)));

        // set actualDepartureTime
        writeField(writer, ACTUAL_DEPARTURE_TIME, toLocalTime(source.getDate(ACTUAL_DEPARTURE_TIME)));

        // set expectedDepartureTime
        writeField(writer, EXPECTED_DEPARTURE_TIME, toLocalTime(source.getDate(EXPECTED_DEPARTURE_TIME)));

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
        writeField(writer, DEPARTURE_PLATFORM_NAME, source.getString(DEPARTURE_PLATFORM_NAME));

        // departureBoardingActivity :byte;

        // departureStopAssignment :StopAssignment;

        // departureOperatorRefs

    }
}