package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.Call;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public class VehicleDepartureTimesGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new VehicleDepartureTimesGroupMarshaller();

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

    }
}
