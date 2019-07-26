package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.Destination;

import java.io.IOException;

public class DestinationMarshaller implements Marshaller<Destination> {

    @Getter
    private static final Marshaller<Destination> instance = new DestinationMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, Destination source) throws IOException {
        write(writer, () -> {
            write(writer, "destinationRef", source.destinationRef());
            write(writer, "placeName", source.placeName());
            // directionRef :string;
        });
    }
}
