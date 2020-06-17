package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class CallRailGroupMarshaller implements Marshaller<Document> {

    private static final String PLATFORM_TRAVERSAL = "platformTraversal";
    @Getter
    private static final Marshaller<Document> instance = new CallRailGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // reversesAtStop :bool;

        // set platformTraversal
        writeField(writer, PLATFORM_TRAVERSAL, source.getBoolean(PLATFORM_TRAVERSAL));

        // signalStatus :string;
    }

}