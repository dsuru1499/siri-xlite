package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.util.List;

public class ServiceInfoGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new ServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set operatorRef
        writeField(writer, "OperatorRef", source.getString("operatorRef"));

        // set productCategoryRef
        writeField(writer, "ProductCategoryRef", source.getString("productCategoryRef"));

        // set serviceFeatureRef
        writeArray(writer, "ServiceFeatureRefs", source.get("serviceFeatureRefs", List.class));

        // set vehicleFeatureRef
        writeArray(writer, "VehicleFeatureRef", source.get("vehicleFeatureRefs", List.class));

    }
}