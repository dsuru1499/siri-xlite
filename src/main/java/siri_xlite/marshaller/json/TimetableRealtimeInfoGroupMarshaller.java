package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class TimetableRealtimeInfoGroupMarshaller implements Marshaller<Document> {

    private static final String MONITORED = "monitored";
    private static final String HEADWAY_SERVICE = "headwayService";
    @Getter
    private static final Marshaller<Document> instance = new TimetableRealtimeInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set monitored
        writeField(writer, MONITORED, source.getBoolean(MONITORED));

        // set headwayService
        writeField(writer, HEADWAY_SERVICE, source.getBoolean(HEADWAY_SERVICE));

    }

}