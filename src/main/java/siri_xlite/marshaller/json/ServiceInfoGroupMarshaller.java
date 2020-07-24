package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import static siri_xlite.common.JsonUtils.writeArrayField;
import static siri_xlite.common.JsonUtils.writeStringField;

public class ServiceInfoGroupMarshaller implements Marshaller<Document> {

    public static final String OPERATOR_REF = "operatorRef";
    public static final String PRODUCT_CATEGORY_REF = "productCategoryRef";
    public static final String SERVICE_FEATURE_REFS = "serviceFeatureRefs";
    public static final String VEHICLE_FEATURE_REFS = "vehicleFeatureRefs";

    @Getter
    private static final Marshaller<Document> instance = new ServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set operatorRef
        writeStringField(writer, OPERATOR_REF, source);

        // set productCategoryRef
        writeStringField(writer, PRODUCT_CATEGORY_REF, source);

        // set serviceFeatureRef
        writeArrayField(writer, SERVICE_FEATURE_REFS, source);

        // set vehicleFeatureRef
        writeArrayField(writer, VEHICLE_FEATURE_REFS, source);

    }
}