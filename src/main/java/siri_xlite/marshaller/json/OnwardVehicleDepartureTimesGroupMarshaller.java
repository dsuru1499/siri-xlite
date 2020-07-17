package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.DateTimeUtils.toLocalTime;

public class OnwardVehicleDepartureTimesGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";

    @Getter
    private static final Marshaller<Document> instance = new OnwardVehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, toLocalTime(source.getDate(AIMED_DEPARTURE_TIME)));

        // set expectedDepartureTime
        writeField(writer, EXPECTED_DEPARTURE_TIME, toLocalTime(source.getDate(EXPECTED_DEPARTURE_TIME)));

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;
    }
}
