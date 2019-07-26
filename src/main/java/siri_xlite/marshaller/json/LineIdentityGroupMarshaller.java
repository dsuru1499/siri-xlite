package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;

public class LineIdentityGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new LineIdentityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set lineRef
        String lineRef = source.lineRef();
        if (!StringUtils.isEmpty(lineRef)) {
            writer.writeStringField("LineRef", lineRef);
        }

        // set directionRef
        String directionRef = source.directionRef();
        if (!StringUtils.isEmpty(directionRef)) {
            writer.writeStringField("DirectionRef", directionRef);
        }
    }
}
