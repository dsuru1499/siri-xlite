package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.StopPoint;

import java.io.IOException;

public class StopPointMarshaller implements Marshaller<StopPoint> {

    @Getter
    private static final Marshaller<StopPoint> instance = new StopPointMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, StopPoint source) throws IOException {
        // timingPoint :bool;
        // monitored :bool;
        write(writer, "stopPointRef", source.stopPointRef());
        write(writer, "stopName", source.stopName());
        // stopAreaRef ;
        ;
        write(writer, "lineRefs", source.lineRefs());
        // features : [Feature];
        LocationMarshaller.getInstance().write(writer, source.location());
        // url ;
    }

}
