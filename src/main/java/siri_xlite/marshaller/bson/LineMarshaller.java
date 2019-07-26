package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.Destination;
import siri_xlite.model.Line;

import java.io.IOException;

public class LineMarshaller implements Marshaller<Line> {

    @Getter
    private static final Marshaller<Line> instance = new LineMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, Line source) throws IOException {
        write(writer, () -> {
            write(writer, "lineRef", source.lineRef());
            write(writer, "lineName", source.lineName());
            write(writer, "destinations", source.destinations(),
                    wrapper((Destination t) -> DestinationMarshaller.getInstance().write(writer, t)));
        });
    }

}
