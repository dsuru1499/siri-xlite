package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.Call;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public class OnwardVehicleArrivalTimesGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new OnwardVehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set aimedArrivalTime
        long aimedArrivalTime = source.aimedArrivalTime();
        writer.writeStringField("AimedArrivalTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(aimedArrivalTime)));

        // set expectedArrivalTime
        long expectedArrivalTime = source.expectedArrivalTime();
        writer.writeStringField("ExpectedArrivalTime",
                SiriStructureFactory.createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(expectedArrivalTime)));

        // expectedArrivalPredictionQuality :PredictionQuality;

    }

}
