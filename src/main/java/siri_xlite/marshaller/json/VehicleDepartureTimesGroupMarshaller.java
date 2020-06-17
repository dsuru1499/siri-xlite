package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class VehicleDepartureTimesGroupMarshaller implements Marshaller<Document> {

    private static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    private static final String ACTUAL_DEPARTURE_TIME = "actualDepartureTime";
    private static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";
    @Getter
    private static final Marshaller<Document> instance = new VehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, source.getDate(AIMED_DEPARTURE_TIME));

        // set actualDepartureTime
        writeField(writer, ACTUAL_DEPARTURE_TIME, source.getDate(ACTUAL_DEPARTURE_TIME));

        // set expectedDepartureTime
        writeField(writer, EXPECTED_DEPARTURE_TIME, source.getDate(EXPECTED_DEPARTURE_TIME));

    }
}
