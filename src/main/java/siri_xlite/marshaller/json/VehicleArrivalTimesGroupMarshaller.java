package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class VehicleArrivalTimesGroupMarshaller implements Marshaller<Document> {

    private static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    private static final String ACTUAL_ARRIVAL_TIME = "actualArrivalTime";
    private static final String EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";
    @Getter
    private static final Marshaller<Document> instance = new VehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, source.getDate(AIMED_ARRIVAL_TIME));

        // set actualArrivalTime
        writeField(writer, ACTUAL_ARRIVAL_TIME, source.getDate(ACTUAL_ARRIVAL_TIME));

        // set expectedArrivalTime
        writeField(writer, EXPECTED_ARRIVAL_TIME, source.getDate(EXPECTED_ARRIVAL_TIME));

    }
}
