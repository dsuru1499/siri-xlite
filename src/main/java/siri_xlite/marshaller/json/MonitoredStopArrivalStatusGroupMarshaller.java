package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

public class MonitoredStopArrivalStatusGroupMarshaller implements Marshaller<Document> {

    public static final String ARRIVAL_STATUS = "arrivalStatus";
    public static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";

    @Getter
    private static final Marshaller<Document> instance = new MonitoredStopArrivalStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set arrivalStatus
        Integer arrivalStatus = source.getInteger(ARRIVAL_STATUS);
        if (arrivalStatus != null) {
            writeField(writer, ARRIVAL_STATUS, CallStatusEnumeration.values()[arrivalStatus]);
        }
        // arrivalProximityText :string;

        // set arrivalPlatformName
        writeField(writer, ARRIVAL_PLATFORM_NAME, source.getString(ARRIVAL_PLATFORM_NAME));

        // arrivalBoardingActivity :byte;
        // arrivalStopAssignment :StopAssignment;

        // set arrivalOperatorRefs

    }

}
