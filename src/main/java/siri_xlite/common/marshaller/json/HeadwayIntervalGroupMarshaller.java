package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeDurationField;

public class HeadwayIntervalGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_HEADWAY_INTERVAL = "aimedHeadwayInterval";
    public static final String EXPECTED_HEADWAY_INTERVAL = "expectedHeadwayInterval";

    @Getter
    private static final Marshaller<Document> instance = new HeadwayIntervalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedHeadwayInterval
        writeDurationField(writer, AIMED_HEADWAY_INTERVAL, source);

        // set expectedHeadwayInterval
        writeDurationField(writer, EXPECTED_HEADWAY_INTERVAL, source);

    }
}