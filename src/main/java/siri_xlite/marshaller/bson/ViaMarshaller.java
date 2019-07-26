package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.Via;

import java.io.IOException;

public class ViaMarshaller implements Marshaller<Via> {

    @Getter
    private static final Marshaller<Via> instance = new ViaMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, Via source) throws IOException {
        if (source != null) {
            writer.writeStartObject();
            write(writer, "placeRef", source.placeRef());
            write(writer, "placeName", source.placeName());
            writer.writeEndObject();
        }
    }

}
