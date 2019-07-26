package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;
import uk.org.siri.siri.CallStatusEnumeration;

import java.io.IOException;

public class MonitoredStopArrivalStatusGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new MonitoredStopArrivalStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set arrivalStatus
        int arrivalStatus = source.arrivalStatus();
        writer.writeStringField("ArrivalStatus", CallStatusEnumeration.values()[arrivalStatus].name());

        // arrivalProximityText :string;

        // set arrivalPlatformName
        String arrivalPlatformName = source.arrivalPlatformName();
        if (arrivalPlatformName != null && !arrivalPlatformName.isEmpty()) {
            writer.writeStringField("ArrivalPlatformName", arrivalPlatformName);
        }

        // arrivalBoardingActivity :byte;
        // arrivalStopAssignment :StopAssignment;

        // set arrivalOperatorRefs

    }

}
