package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.Call;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.CallStatusEnumeration;

import java.io.IOException;

public class StopArrivalGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new StopArrivalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set aimedArrivalTime
        long aimedArrivalTime = source.aimedArrivalTime();
        writer.writeStringField("AimedArrivalTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(aimedArrivalTime)));

        // set actualArrivalTime
        long actualArrivalTime = source.actualArrivalTime();
        writer.writeStringField("ActualArrivalTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(actualArrivalTime)));

        // set expectedArrivalTime
        long expectedArrivalTime = source.expectedArrivalTime();
        writer.writeStringField("ExpectedArrivalTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(expectedArrivalTime)));

        // set latestExpectedArrivalTime

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