package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.DateTimeUtils.toLocalTime;

public class VehicleArrivalTimesGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String ACTUAL_ARRIVAL_TIME = "actualArrivalTime";
    public static final String EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";

    @Getter
    private static final Marshaller<Document> instance = new VehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, toLocalTime(source.getDate(AIMED_ARRIVAL_TIME)));

        // set actualArrivalTime
        writeField(writer, ACTUAL_ARRIVAL_TIME, toLocalTime(source.getDate(ACTUAL_ARRIVAL_TIME)));

        // set expectedArrivalTime
        writeField(writer, EXPECTED_ARRIVAL_TIME, toLocalTime(source.getDate(EXPECTED_ARRIVAL_TIME)));

    }
}
