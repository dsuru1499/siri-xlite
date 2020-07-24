package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri_xlite.common.JsonUtils.writeField;
import static siri_xlite.common.JsonUtils.writeStringField;

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
        writeStringField(writer, ARRIVAL_PLATFORM_NAME, source);

        // arrivalBoardingActivity :byte;
        // arrivalStopAssignment :StopAssignment;

        // set arrivalOperatorRefs

    }

}
