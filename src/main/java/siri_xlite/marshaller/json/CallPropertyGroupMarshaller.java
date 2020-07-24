package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeStringField;

public class CallPropertyGroupMarshaller implements Marshaller<Document> {

    public static final String ORIGIN_DISPLAY = "originDisplay";
    public static final String DESTINATION_DISPLAY = "destinationDisplay";

    @Getter
    private static final Marshaller<Document> instance = new CallPropertyGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // timingPoint :bool;
        // boardingStretch :bool;
        // requestStop :bool;

        // set originDisplay
        writeStringField(writer, ORIGIN_DISPLAY, source);

        // set destinationDisplay
        writeStringField(writer, DESTINATION_DISPLAY, source);

    }

}
