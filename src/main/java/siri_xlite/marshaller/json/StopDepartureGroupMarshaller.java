package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.Call;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.CallStatusEnumeration;

import java.io.IOException;

public class StopDepartureGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new StopDepartureGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set aimedDepartureTime
        long aimedDepartureTime = source.aimedDepartureTime();
        writer.writeStringField("AimedDepartureTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(aimedDepartureTime)));

        // set actualDepartureTime
        long actualDepartureTime = source.actualDepartureTime();
        writer.writeStringField("ActualDepartureTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(actualDepartureTime)));

        // set expectedDepartureTime
        long expectedDepartureTime = source.expectedDepartureTime();
        writer.writeStringField("ExpectedDepartureTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(expectedDepartureTime)));

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;

        // aimedLatestPassengerAccessTime :long;

        // expectedLatestPassengerAccessTime :long;

        // set departureStatus
        int departureStatus = source.departureStatus();
        writer.writeStringField("DepartureStatus", CallStatusEnumeration.values()[departureStatus].name());

        // departureProximityText :string;

        // set departurePlatformName
        String departurePlatformName = source.departurePlatformName();
        if (!StringUtils.isEmpty(departurePlatformName)) {
            writer.writeStringField("DeparturePlatformName", departurePlatformName);
        }

        // departureBoardingActivity :byte;

        // departureStopAssignment :StopAssignment;

        // departureOperatorRefs

    }
}