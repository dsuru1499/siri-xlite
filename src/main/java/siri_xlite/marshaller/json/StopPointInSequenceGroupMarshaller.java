package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class StopPointInSequenceGroupMarshaller implements Marshaller<Document> {
    public static final String ORDER = "order";
    public static final String STOP_POINT_REF = "stopPointRef";
    public static final String STOP_POINT_NAME = "stopPointName";

    @Getter
    private static final Marshaller<Document> instance = new StopPointInSequenceGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set stopPointRef
        writeField(writer, STOP_POINT_REF, source.getString(STOP_POINT_REF));

        // visitNumber :ushort;

        // set order
        writeField(writer, ORDER, source.getInteger(ORDER));

        // set stopPointName
        writeField(writer, STOP_POINT_NAME, source.getString(STOP_POINT_NAME));

    }
}
