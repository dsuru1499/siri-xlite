package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class LineIdentityGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new LineIdentityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set lineRef
        writeField(writer, "LineRef", source.getString("lineRef"));

        // set directionRef
        writeField(writer, "DirectionRef", source.getString("directionRef"));
    }
}