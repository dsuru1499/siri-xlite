package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import siri_xlite.service.common.SiriStructureFactory;

public class HeadwayIntervalGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_HEADWAY_INTERVAL = "aimedHeadwayInterval";
    public static final String EXPECTED_HEADWAY_INTERVAL = "expectedHeadwayInterval";

    @Getter
    private static final Marshaller<Document> instance = new HeadwayIntervalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedHeadwayInterval
        Long aimedHeadwayInterval = source.getLong(AIMED_HEADWAY_INTERVAL);
        writeField(writer, AIMED_HEADWAY_INTERVAL, SiriStructureFactory.createDuration(aimedHeadwayInterval));

        // set expectedHeadwayInterval
        Long expectedHeadwayInterval = source.getLong(EXPECTED_HEADWAY_INTERVAL);
        writeField(writer, EXPECTED_HEADWAY_INTERVAL, SiriStructureFactory.createDuration(expectedHeadwayInterval));

    }
}