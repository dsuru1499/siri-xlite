package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class StopPointInSequenceGroupMarshaller implements Marshaller<Document> {
    @Getter
    private static final Marshaller<Document> instance = new StopPointInSequenceGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set stopPointRef
        writeField(writer, "StopPointRef", source.getString("stopPointRef"));

        // visitNumber :ushort;

        // set order
        writeField(writer, "Order", source.getInteger("order"));

        // set stopPointName
        writeField(writer, "StopPointName", source.getString("stopPointName"));

    }
}
