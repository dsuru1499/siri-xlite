package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeLocalTimeField;

public class OnwardVehicleDepartureTimesGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";

    @Getter
    private static final Marshaller<Document> instance = new OnwardVehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeLocalTimeField(writer, AIMED_DEPARTURE_TIME, source);

        // set expectedDepartureTime
        writeLocalTimeField(writer, EXPECTED_DEPARTURE_TIME, source);

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;
    }
}
