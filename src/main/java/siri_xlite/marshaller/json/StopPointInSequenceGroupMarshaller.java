package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;

import java.io.IOException;

public class StopPointInSequenceGroupMarshaller implements Marshaller<Call> {
    @Getter
    private static final Marshaller<Call> instance = new StopPointInSequenceGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // set stopPointRef
        String stopPointRef = source.stopPointRef();
        if (stopPointRef != null && !stopPointRef.isEmpty()) {
            writer.writeStringField("StopPointRef", stopPointRef);
        }

        // visitNumber :ushort;

        // set order
        int order = source.order();
        writer.writeNumberField("Order", order);

        // set stopPointName
        String stopPointName = source.stopPointName();
        if (stopPointName != null && !stopPointName.isEmpty()) {
            writer.writeStringField("StopPointName", stopPointName);
        }
    }
}
