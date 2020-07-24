package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeBooleanField;

public class CallRailGroupMarshaller implements Marshaller<Document> {

    public static final String PLATFORM_TRAVERSAL = "platformTraversal";

    @Getter
    private static final Marshaller<Document> instance = new CallRailGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // reversesAtStop :bool;

        // set platformTraversal
        writeBooleanField(writer, PLATFORM_TRAVERSAL, source);

        // signalStatus :string;
    }

}