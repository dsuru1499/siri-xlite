package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

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
        writeField(writer, AIMED_ARRIVAL_TIME, source.getDate(AIMED_ARRIVAL_TIME));

        // set actualArrivalTime
        writeField(writer, ACTUAL_ARRIVAL_TIME, source.getDate(ACTUAL_ARRIVAL_TIME));

        // set expectedArrivalTime
        writeField(writer, EXPECTED_ARRIVAL_TIME, source.getDate(EXPECTED_ARRIVAL_TIME));

        // set latestExpectedArrivalTime

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