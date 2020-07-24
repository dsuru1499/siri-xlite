package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeLongField;

public class StopProximityGroupMarshaller implements Marshaller<Document> {

    public static final String DISTANCE_FROM_STOP = "distanceFromStop";
    public static final String NUMBER_OF_STOPS_AWAY = "numberOfStopsAway";

    @Getter
    private static final Marshaller<Document> instance = new StopProximityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set distanceFromStop
        writeLongField(writer, DISTANCE_FROM_STOP, source);

        // set numberOfStopsAway
        writeLongField(writer, NUMBER_OF_STOPS_AWAY, source);

    }

}