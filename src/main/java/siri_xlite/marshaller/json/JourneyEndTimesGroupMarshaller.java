package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.VehicleJourney;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

import java.io.IOException;

public class JourneyEndTimesGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyEndTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set headwayService
        boolean headwayService = source.headwayService();
        writer.writeBooleanField("HeadwayService", headwayService);

        // set originAimedDepartureTime
        long originAimedDepartureTime = source.originAimedDepartureTime();
        writer.writeStringField("OriginAimedDepartureTime", SiriStructureFactory
                .createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(originAimedDepartureTime)));

        // set destinationAimedArrivalTime
        long destinationAimedArrivalTime = source.destinationAimedArrivalTime();
        writer.writeStringField("DestinationAimedArrivalTime", SiriStructureFactory
                .createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(destinationAimedArrivalTime)));

        // set firstOrLastJourney
        int firstOrLastJourney = source.firstOrLastJourney();
        writer.writeStringField("FirstOrLastJourney",
                FirstOrLastJourneyEnumeration.values()[firstOrLastJourney].name());

    }
}