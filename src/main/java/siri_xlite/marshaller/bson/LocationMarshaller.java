package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.Location;

import java.io.IOException;

public class LocationMarshaller implements Marshaller<Location> {

    @Getter
    private static final Marshaller<Location> instance = new LocationMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, Location source) throws IOException {
        if (source != null) {
            writer.writeObjectFieldStart("vehicleLocation");
            write(writer, "longitude", source.longitude());
            write(writer, "latitude", source.latitude());
            writer.writeEndObject();
        }
    }

}
