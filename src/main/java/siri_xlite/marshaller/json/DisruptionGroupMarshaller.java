package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class DisruptionGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new DisruptionGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // facilityConditionElement :[FacilityCondition];
        // facilityChangeElement :[FacilityChange];

        // set situationRef
        List<String> situationRefs = source.get("situationRefs", List.class);
        writeArray(writer, "SituationRef", situationRefs,
                t -> writeObject(writer, t, situationRef -> writeField(writer, "SituationSimpleRef", situationRef)));
    }

}
