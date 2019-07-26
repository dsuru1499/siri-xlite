package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.model.Call;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;
import java.util.List;

public class DisruptionGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new DisruptionGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // facilityConditionElement :[FacilityCondition];
        // facilityChangeElement :[FacilityChange];

        // set situationRef
        List<String> situationRefs = source.situationRefs();
        if (situationRefs.size() > 0) {
            writer.writeArrayFieldStart("SituationRef");
            for (String situationRef : situationRefs) {
                writer.writeStartObject();
                writer.writeStringField("SituationSimpleRef", situationRef);
                writer.writeEndObject();
            }
            writer.writeEndObject();
        }
    }

    public void write(Parameters parameters, Call source, JsonGenerator writer) throws IOException {

        // facilityConditionElement :[FacilityCondition];
        // facilityChangeElement :[FacilityChange];

        // set situationRef
        List<String> situationRefs = source.situationRefs();
        if (situationRefs.size() > 0) {
            writer.writeArrayFieldStart("SituationRef");
            for (String situationRef : situationRefs) {
                writer.writeStartObject();
                writer.writeStringField("SituationSimpleRef", situationRef);
                writer.writeEndObject();
            }
            writer.writeEndObject();
        }
    }

}
