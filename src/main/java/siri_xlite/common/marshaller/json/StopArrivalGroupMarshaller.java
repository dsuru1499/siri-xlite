package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri_xlite.common.JsonUtils.*;

public class StopArrivalGroupMarshaller implements Marshaller<Document> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String ACTUAL_ARRIVAL_TIME = "actualArrivalTime";
    public static final String EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";
    public static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";
    public static final String ARRIVAL_STATUS = "arrivalStatus";

    @Getter
    private static final Marshaller<Document> instance = new StopArrivalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeLocalTimeField(writer, AIMED_ARRIVAL_TIME, source);

        // set actualArrivalTime
        writeLocalTimeField(writer, ACTUAL_ARRIVAL_TIME, source);

        // set expectedArrivalTime
        writeLocalTimeField(writer, EXPECTED_ARRIVAL_TIME, source);

        // set latestExpectedArrivalTime

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