package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.Call;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.DepartureBoardingActivityEnumeration;

import java.io.IOException;

public class AimedCallGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new AimedCallGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set aimedArrivalTime
        long aimedArrivalTime = source.aimedArrivalTime();
        writer.writeStringField("AimedArrivalTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(aimedArrivalTime)));

        // set arrivalPlatformName
        String arrivalPlatformName = source.arrivalPlatformName();
        if (!StringUtils.isEmpty(arrivalPlatformName)) {
            writer.writeStringField("ArrivalPlatformName", arrivalPlatformName);
        }

        // arrivalBoardingActivity :byte;

        // set arrivalOperatorRefs

        // set aimedDepartureTime
        long aimedDepartureTime = source.aimedDepartureTime();
        writer.writeStringField("AimedDepartureTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(aimedDepartureTime)));

        // set departurePlatformName
        String departurePlatformName = source.departurePlatformName();
        if (!StringUtils.isEmpty(departurePlatformName)) {
            writer.writeStringField("ArrivalPlatformName", departurePlatformName);
        }

        // set departureBoardingActivity
        int departureBoardingActivity = source.departureBoardingActivity();
        writer.writeStringField("DepartureBoardingActivity",
                DepartureBoardingActivityEnumeration.values()[departureBoardingActivity].name());

        // set departureOperatorRefs

        // aimedLatestPassengerAccessTime :long;

        long aimedHeadwayInterval = source.aimedHeadwayInterval();
        writer.writeStringField("AimedHeadwayInterval", SiriStructureFactory.createDuration(aimedHeadwayInterval));

    }

}
