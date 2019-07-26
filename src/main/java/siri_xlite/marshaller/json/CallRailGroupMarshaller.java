package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;

import java.io.IOException;

public class CallRailGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new CallRailGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        // reversesAtStop :bool;

        // set platformTraversal
        boolean platformTraversal = source.platformTraversal();
        writer.writeBooleanField("PlatformTraversal", platformTraversal);

        // signalStatus :string;
    }

}