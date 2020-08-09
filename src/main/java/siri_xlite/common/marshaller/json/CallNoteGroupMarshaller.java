package siri_xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class CallNoteGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new CallNoteGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // callNote :[string];
    }

}