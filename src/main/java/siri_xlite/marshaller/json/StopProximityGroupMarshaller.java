package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;

import java.io.IOException;

public class StopProximityGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new StopProximityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set distanceFromStop
        long distanceFromStop = source.distanceFromStop();
        writer.writeNumberField("DistanceFromStop", distanceFromStop);

        // set numberOfStopsAway
        long numberOfStopsAway = source.numberOfStopsAway();
        writer.writeNumberField("NumberOfStopsAway", numberOfStopsAway);

    }

}