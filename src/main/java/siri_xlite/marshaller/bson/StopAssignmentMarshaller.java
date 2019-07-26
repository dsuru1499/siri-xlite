package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.StopAssignment;

import java.io.IOException;

public class StopAssignmentMarshaller implements Marshaller<StopAssignment> {

    @Getter
    private static final Marshaller<StopAssignment> instance = new StopAssignmentMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, StopAssignment source) throws IOException {
        write(writer, "aimedQuayRef", source.aimedQuayRef());
        write(writer, "aimedQuayName", source.aimedQuayName());
        write(writer, "expectedQuayRef", source.expectedQuayRef());
        write(writer, "actualQuayRef", source.actualQuayRef());
    }

}
