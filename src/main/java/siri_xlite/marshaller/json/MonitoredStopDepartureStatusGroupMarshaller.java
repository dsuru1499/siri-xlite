package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

public class MonitoredStopDepartureStatusGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new MonitoredStopDepartureStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, org.bson.Document source) {

        // set departureStatus
        int departureStatus = source.getInteger("departureStatus");
        writeField(writer, "DepartureStatus", CallStatusEnumeration.values()[departureStatus].name());

        // departureProximityText :string;

        // set departurePlatformName
        writeField(writer, "DeparturePlatformName", source.getString("departurePlatformName"));

        // set departureBoardingActivity
        writeField(writer, "departureBoardingActivity", source.getInteger("departureBoardingActivity"));

        // departureStopAssignment :StopAssignment;

        // set departureOperatorRefs

    }
}