package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;
import uk.org.siri.siri.CallStatusEnumeration;

import java.io.IOException;

public class MonitoredStopDepartureStatusGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new MonitoredStopDepartureStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set departureStatus
        int departureStatus = source.departureStatus();
        writer.writeStringField("DepartureStatus", CallStatusEnumeration.values()[departureStatus].name());

        // departureProximityText :string;

        // set departurePlatformName
        String departurePlatformName = source.departurePlatformName();
        if (departurePlatformName != null && !departurePlatformName.isEmpty()) {
            writer.writeStringField("DeparturePlatformName", departurePlatformName);
        }

        // set departureBoardingActivity
        int departureBoardingActivity = source.departureBoardingActivity();
        writer.writeNumberField("DepartureBoardingActivity", departureBoardingActivity);

        // departureStopAssignment :StopAssignment;

        // set departureOperatorRefs

    }
}