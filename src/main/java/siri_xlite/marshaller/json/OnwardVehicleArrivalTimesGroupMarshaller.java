package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class OnwardVehicleArrivalTimesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new OnwardVehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, "AimedArrivalTime", source.getDate("aimedArrivalTime"));

        // set expectedArrivalTime
        writeField(writer, "ExpectedArrivalTime", source.getDate("expectedArrivalTime"));

        // expectedArrivalPredictionQuality :PredictionQuality;

    }

}
