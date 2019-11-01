package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class OnwardVehicleArrivalTimesGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";
    @Getter
    private static final Marshaller<Document> instance = new OnwardVehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, source.getDate(AIMED_ARRIVAL_TIME));

        // set expectedArrivalTime
        writeField(writer, EXPECTED_ARRIVAL_TIME, source.getDate(EXPECTED_ARRIVAL_TIME));

        // expectedArrivalPredictionQuality :PredictionQuality;

    }

}
