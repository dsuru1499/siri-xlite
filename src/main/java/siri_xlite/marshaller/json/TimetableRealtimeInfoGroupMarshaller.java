package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class TimetableRealtimeInfoGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new TimetableRealtimeInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set monitored
        writeField(writer, "Monitored", source.getBoolean("monitored"));

        // set headwayService
        writeField(writer, "HeadwayService", source.getBoolean("headwayService"));

    }

}