package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

public class MonitoredStopDepartureStatusGroupMarshaller implements Marshaller<Document> {

    public static final String DEPARTURE_STATUS = "departureStatus";
    public static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";
    public static final String DEPARTURE_BOARDING_ACTIVITY = "departureBoardingActivity";

    @Getter
    private static final Marshaller<Document> instance = new MonitoredStopDepartureStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, org.bson.Document source) {

        // set departureStatus
        Integer departureStatus = source.getInteger(DEPARTURE_STATUS);
        if (departureStatus != null) {
            writeField(writer, DEPARTURE_STATUS, CallStatusEnumeration.values()[departureStatus]);
        }

        // departureProximityText :string;

        // set departurePlatformName
        writeField(writer, DEPARTURE_PLATFORM_NAME, source.getString(DEPARTURE_PLATFORM_NAME));

        // set departureBoardingActivity
        writeField(writer, DEPARTURE_BOARDING_ACTIVITY, source.getInteger(DEPARTURE_BOARDING_ACTIVITY));

        // departureStopAssignment :StopAssignment;

        // set departureOperatorRefs

    }
}