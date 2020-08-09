package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeBooleanField;

public class CallRealtimeGroupMarshaller implements Marshaller<Document> {

    public static final String VEHICLE_AT_STOP = "vehicleAtStop";

    @Getter
    private static final Marshaller<Document> instance = new CallRealtimeGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set vehicleAtStop
        writeBooleanField(writer, VEHICLE_AT_STOP, source);

        // vehicleLocationAtStop : Location;
    }

}