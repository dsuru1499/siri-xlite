package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;
import java.util.List;

public class ServiceInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new ServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set operatorRef
        String operatorRef = source.operatorRef();
        if (operatorRef != null && !operatorRef.isEmpty()) {
            writer.writeStringField("OperatorRef", operatorRef);
        }

        // set productCategoryRef
        String productCategoryRef = source.productCategoryRef();
        if (productCategoryRef != null && !productCategoryRef.isEmpty()) {
            writer.writeStringField("ProductCategoryRef", productCategoryRef);
        }

        // set serviceFeatureRef
        List<String> serviceFeatureRefs = source.serviceFeatureRefs();
        if (!CollectionUtils.isEmpty(serviceFeatureRefs)) {
            writer.writeArrayFieldStart("ServiceFeatureRef");
            for (String serviceFeatureRef : serviceFeatureRefs) {
                writer.writeString(serviceFeatureRef);
            }
            writer.writeEndArray();
        }

        // set vehicleFeatureRef
        List<String> vehicleFeatureRefs = source.vehicleFeatureRefs();
        if (!CollectionUtils.isEmpty(vehicleFeatureRefs)) {
            writer.writeArrayFieldStart("VehicleFeatureRef");
            for (String vehicleFeatureRef : vehicleFeatureRefs) {
                writer.writeString(vehicleFeatureRef);
            }
            writer.writeEndArray();
        }
    }
}