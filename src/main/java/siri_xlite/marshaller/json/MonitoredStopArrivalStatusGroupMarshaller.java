package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

public class MonitoredStopArrivalStatusGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new MonitoredStopArrivalStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set arrivalStatus
        int arrivalStatus = source.getInteger("arrivalStatus");
        writeField(writer, "ArrivalPlatformName", CallStatusEnumeration.values()[arrivalStatus].name());

        // arrivalProximityText :string;

        // set arrivalPlatformName
        writeField(writer, "ArrivalPlatformName", source.getString("arrivalPlatformName"));

        // arrivalBoardingActivity :byte;
        // arrivalStopAssignment :StopAssignment;

        // set arrivalOperatorRefs

    }

}
