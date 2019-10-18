package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class CallPropertyGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new CallPropertyGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // timingPoint :bool;
        // boardingStretch :bool;
        // requestStop :bool;

        // set originDisplay
        writeField(writer, "OriginDisplay", source.getString("originDisplay"));

        // set destinationDisplay
        writeField(writer, "DestinationDisplay", source.getString("destinationDisplay"));

    }

}
