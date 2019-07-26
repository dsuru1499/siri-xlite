package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.DistributorConnectionLink;

import java.io.IOException;

public class DistributorConnectionLinkMarshaller implements Marshaller<DistributorConnectionLink> {

    @Getter
    private static final Marshaller<DistributorConnectionLink> instance = new DistributorConnectionLinkMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, DistributorConnectionLink source)
            throws IOException {
        if (source != null) {
            writer.writeObjectFieldStart("distributorConnectionLink");

            write(writer, "connectionLinkCode", source.connectionLinkCode());
            write(writer, "stopPointRef", source.stopPointRef());
            write(writer, "stopPointName", source.stopPointName());
            write(writer, "defaultDuration", source.defaultDuration());
            write(writer, "frequentTravellerDuration", source.frequentTravellerDuration());
            write(writer, "occasionalTravellerDuration", source.occasionalTravellerDuration());
            write(writer, "impairedAccessDuration", source.impairedAccessDuration());

            writer.writeEndObject();
        }
    }

}
