package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class CallRealtimeGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new CallRealtimeGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set vehicleAtStop
        writeField(writer, "VehicleAtStop", source.getBoolean("vehicleAtStop"));

        // vehicleLocationAtStop : Location;
    }

}