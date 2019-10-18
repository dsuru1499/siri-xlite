package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

public class StopDepartureGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new StopDepartureGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedDepartureTime
        writeField(writer, "AimedDepartureTime", source.getDate("aimedDepartureTime"));

        // set actualDepartureTime
        writeField(writer, "ActualDepartureTime", source.getDate("actualDepartureTime"));

        // set expectedDepartureTime
        writeField(writer, "ExpectedDepartureTime", source.getDate("expectedDepartureTime"));

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;

        // aimedLatestPassengerAccessTime :long;

        // expectedLatestPassengerAccessTime :long;

        // set departureStatus
        int departureStatus = source.getInteger("departureStatus");
        writeField(writer, "DepartureStatus", CallStatusEnumeration.values()[departureStatus].name());

        // departureProximityText :string;

        // set departurePlatformName
        writeField(writer, "DeparturePlatformName", source.getString("departurePlatformName"));

        // departureBoardingActivity :byte;

        // departureStopAssignment :StopAssignment;

        // departureOperatorRefs

    }
}