package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class PassengerDepartureTimesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new PassengerDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {
        // aimedLatestPassengerAccessTime :long;
        // expectedLatestPassengerAccessTime :long;
    }

}