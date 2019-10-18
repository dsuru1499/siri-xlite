package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import siri_xlite.service.common.SiriStructureFactory;

public class HeadwayIntervalGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new HeadwayIntervalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedHeadwayInterval
        long aimedHeadwayInterval = source.getLong("aimedHeadwayInterval");
        writeField(writer, "AimedHeadwayInterval", SiriStructureFactory.createDuration(aimedHeadwayInterval));

        // set expectedHeadwayInterval
        long expectedHeadwayInterval = source.getLong("aimedHeadwayInterval");
        writeField(writer, "ExpectedHeadwayInterval", SiriStructureFactory.createDuration(expectedHeadwayInterval));

    }
}