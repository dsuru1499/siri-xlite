package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class CallPropertyGroupMarshaller implements Marshaller<Document> {

    private static final String ORIGIN_DISPLAY = "originDisplay";
    private static final String DESTINATION_DISPLAY = "destinationDisplay";
    @Getter
    private static final Marshaller<Document> instance = new CallPropertyGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // timingPoint :bool;
        // boardingStretch :bool;
        // requestStop :bool;

        // set originDisplay
        writeField(writer, ORIGIN_DISPLAY, source.getString(ORIGIN_DISPLAY));

        // set destinationDisplay
        writeField(writer, DESTINATION_DISPLAY, source.getString(DESTINATION_DISPLAY));

    }

}
