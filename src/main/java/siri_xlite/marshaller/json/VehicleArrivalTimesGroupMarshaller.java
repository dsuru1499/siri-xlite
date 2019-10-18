package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class VehicleArrivalTimesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new VehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, "AimedArrivalTime", source.getDate("aimedArrivalTime"));

        // set actualArrivalTime
        writeField(writer, "ActualArrivalTime", source.getDate("actualArrivalTime"));

        // set expectedArrivalTime
        writeField(writer, "ExpectedArrivalTime", source.getDate("expectedArrivalTime"));

    }
}
