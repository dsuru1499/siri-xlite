package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeLocalTimeField;

public class VehicleDepartureTimesGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String ACTUAL_DEPARTURE_TIME = "actualDepartureTime";
    public static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";

    @Getter
    private static final Marshaller<Document> instance = new VehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeLocalTimeField(writer, AIMED_DEPARTURE_TIME, source);

        // set actualDepartureTime
        writeLocalTimeField(writer, ACTUAL_DEPARTURE_TIME, source);

        // set expectedDepartureTime
        writeLocalTimeField(writer, EXPECTED_DEPARTURE_TIME, source);

    }
}
