package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.JourneyPart;

import java.io.IOException;

public class JourneyPartMarshaller implements Marshaller<JourneyPart> {

    @Getter
    private static final Marshaller<JourneyPart> instance = new JourneyPartMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, JourneyPart source) throws IOException {
        if (source != null) {
            writer.writeStartObject();
            write(writer, "journeyPartRef", source.journeyPartRef());
            write(writer, "trainNumberRef", source.trainNumberRef());
            writer.writeEndObject();
        }
    }

}
