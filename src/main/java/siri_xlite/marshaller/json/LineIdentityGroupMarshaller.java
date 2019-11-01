package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class LineIdentityGroupMarshaller implements Marshaller<Document> {

    public static final String LINE_REF = "lineRef";
    public static final String DIRECTION_REF = "directionRef";
    @Getter
    private static final Marshaller<Document> instance = new LineIdentityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set lineRef
        writeField(writer, LINE_REF, source.getString(LINE_REF));

        // set directionRef
        writeField(writer, DIRECTION_REF, source.getString(DIRECTION_REF));
    }
}