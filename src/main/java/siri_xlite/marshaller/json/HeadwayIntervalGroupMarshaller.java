package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public class HeadwayIntervalGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new HeadwayIntervalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set aimedHeadwayInterval
        long aimedHeadwayInterval = source.aimedHeadwayInterval();
        writer.writeStringField("AimedHeadwayInterval", SiriStructureFactory.createDuration(aimedHeadwayInterval));

        // set expectedHeadwayInterval
        long expectedHeadwayInterval = source.expectedHeadwayInterval();
        writer.writeStringField("ExpectedHeadwayInterval",
                SiriStructureFactory.createDuration(expectedHeadwayInterval));
    }
}