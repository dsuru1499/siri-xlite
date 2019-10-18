package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class StopProximityGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new StopProximityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set distanceFromStop
        writeField(writer, "DistanceFromStop", source.getLong("distanceFromStop"));

        // set numberOfStopsAway
        writeField(writer, "NumberOfStopsAway", source.getLong("numberOfStopsAway"));

    }

}