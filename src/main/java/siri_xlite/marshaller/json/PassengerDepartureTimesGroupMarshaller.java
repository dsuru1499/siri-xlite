package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Call;

public class PassengerDepartureTimesGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new PassengerDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {
        // aimedLatestPassengerAccessTime :long;
        // expectedLatestPassengerAccessTime :long;
    }

}