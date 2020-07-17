package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class CallRealtimeGroupMarshaller implements Marshaller<Document> {

    public static final String VEHICLE_AT_STOP = "vehicleAtStop";

    @Getter
    private static final Marshaller<Document> instance = new CallRealtimeGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set vehicleAtStop
        writeField(writer, VEHICLE_AT_STOP, source.getBoolean(VEHICLE_AT_STOP));

        // vehicleLocationAtStop : Location;
    }

}