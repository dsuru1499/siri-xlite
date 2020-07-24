package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

import static siri_xlite.common.JsonUtils.*;

public class DisruptionGroupMarshaller implements Marshaller<Document> {

    public static final String SITUATION_REFS = "situationRefs";
    public static final String SITUATION_SIMPLE_REF = "situationSimpleRef";

    @Getter
    private static final Marshaller<Document> instance = new DisruptionGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // facilityConditionElement :[FacilityCondition];
        // facilityChangeElement :[FacilityChange];

        // set situationRef

        List<String> situationRefs = source.get(SITUATION_REFS, List.class);
        writeArray(writer, SITUATION_REFS, situationRefs,
                t -> writeObject(writer, t, situationRef -> writeField(writer, SITUATION_SIMPLE_REF, situationRef)));
    }

}
