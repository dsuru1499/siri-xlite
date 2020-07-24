package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeBooleanField;

public class TimetableRealtimeInfoGroupMarshaller implements Marshaller<Document> {

    public static final String MONITORED = "monitored";
    public static final String HEADWAY_SERVICE = "headwayService";

    @Getter
    private static final Marshaller<Document> instance = new TimetableRealtimeInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set monitored
        writeBooleanField(writer, MONITORED, source);

        // set headwayService
        writeBooleanField(writer, HEADWAY_SERVICE, source);

    }

}