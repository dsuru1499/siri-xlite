package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;

import java.io.IOException;

public class CallPropertyGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new CallPropertyGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // timingPoint :bool;
        // boardingStretch :bool;
        // requestStop :bool;

        // set originDisplay
        String originDisplay = source.originDisplay();
        if (originDisplay != null && !originDisplay.isEmpty()) {
            writer.writeStringField("OriginDisplay", originDisplay);
        }

        // set destinationDisplay
        String destinationDisplay = source.destinationDisplay();
        if (destinationDisplay != null && !destinationDisplay.isEmpty()) {
            writer.writeStringField("DestinationDisplay", destinationDisplay);
        }

    }

}
