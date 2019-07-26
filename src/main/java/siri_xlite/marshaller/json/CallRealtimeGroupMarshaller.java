package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;

import java.io.IOException;

public class CallRealtimeGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new CallRealtimeGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set vehicleAtStop
        boolean vehicleAtStop = source.vehicleAtStop();
        writer.writeBooleanField("VehicleAtStop", vehicleAtStop);

        // vehicleLocationAtStop : Location;
    }

}