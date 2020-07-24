package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeIntegerField;
import static siri_xlite.common.JsonUtils.writeStringField;

public class StopPointInSequenceGroupMarshaller implements Marshaller<Document> {
    public static final String ORDER = "order";
    public static final String STOP_POINT_REF = "stopPointRef";
    public static final String STOP_POINT_NAME = "stopPointName";

    @Getter
    private static final Marshaller<Document> instance = new StopPointInSequenceGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set stopPointRef
        writeStringField(writer, STOP_POINT_REF, source);

        // visitNumber :ushort;

        // set order
        writeIntegerField(writer, ORDER, source);

        // set stopPointName
        writeStringField(writer, STOP_POINT_NAME, source);

    }
}
