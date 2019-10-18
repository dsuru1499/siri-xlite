package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class OperationalBlockGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new OperationalBlockGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {
        // blockRef :string;
        // courseOfJourneyRef :string;
    }

}