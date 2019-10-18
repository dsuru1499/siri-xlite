package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.CallStatusEnumeration;

public class StopArrivalGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new StopArrivalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set aimedArrivalTime
        writeField(writer, "AimedArrivalTime", source.getDate("aimedArrivalTime"));

        // set actualArrivalTime
        writeField(writer, "ActualArrivalTime", source.getDate("actualArrivalTime"));

        // set expectedArrivalTime
        writeField(writer, "ExpectedArrivalTime", source.getDate("expectedArrivalTime"));

        // set latestExpectedArrivalTime

        // set arrivalStatus
        int arrivalStatus = source.getInteger("arrivalStatus");
        writeField(writer, "ArrivalStatus", CallStatusEnumeration.values()[arrivalStatus].name());

        // arrivalProximityText :string;

        // set arrivalPlatformName
        writeField(writer, "ArrivalPlatformName", source.getString("arrivalPlatformName"));

        // arrivalBoardingActivity :byte;

        // arrivalStopAssignment :StopAssignment;

        // set arrivalOperatorRefs

    }

}