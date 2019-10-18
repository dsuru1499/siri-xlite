package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class VehicleDepartureTimesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new VehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeField(writer, "AimedDepartureTime", source.getDate("aimedDepartureTime"));

        // set actualDepartureTime
        writeField(writer, "ActualDepartureTime", source.getDate("actualDepartureTime"));

        // set expectedDepartureTime
        writeField(writer, "ExpectedDepartureTime", source.getDate("expectedDepartureTime"));

    }
}
