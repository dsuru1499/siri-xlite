package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class OnwardVehicleDepartureTimesGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new OnwardVehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeField(writer, "AimedDepartureTime", source.getDate("aimedDepartureTime"));

        // set expectedDepartureTime
        writeField(writer, "ExpectedDepartureTime", source.getDate("expectedDepartureTime"));

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;
    }
}
