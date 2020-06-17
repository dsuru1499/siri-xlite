package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class ServiceInfoGroupMarshaller implements Marshaller<Document> {

    public static final String OPERATOR_REF = "operatorRef";
    private static final String PRODUCT_CATEGORY_REF = "productCategoryRef";
    private static final String SERVICE_FEATURE_REFS = "serviceFeatureRefs";
    private static final String VEHICLE_FEATURE_REFS = "vehicleFeatureRefs";
    @Getter
    private static final Marshaller<Document> instance = new ServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set operatorRef
        writeField(writer, OPERATOR_REF, source.getString(OPERATOR_REF));

        // set productCategoryRef
        writeField(writer, PRODUCT_CATEGORY_REF, source.getString(PRODUCT_CATEGORY_REF));

        // set serviceFeatureRef

        writeArray(writer, SERVICE_FEATURE_REFS, source.get(SERVICE_FEATURE_REFS, List.class));

        // set vehicleFeatureRef

        writeArray(writer, VEHICLE_FEATURE_REFS, source.get(VEHICLE_FEATURE_REFS, List.class));

    }
}