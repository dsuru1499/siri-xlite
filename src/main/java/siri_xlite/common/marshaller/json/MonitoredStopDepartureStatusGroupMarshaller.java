package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri_xlite.common.JsonUtils.*;

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
        writeStringField(writer, DEPARTURE_PLATFORM_NAME, source);

        // set departureBoardingActivity
        writeIntegerField(writer, DEPARTURE_BOARDING_ACTIVITY, source);

        // departureStopAssignment :StopAssignment;

        // set departureOperatorRefs

    }
}