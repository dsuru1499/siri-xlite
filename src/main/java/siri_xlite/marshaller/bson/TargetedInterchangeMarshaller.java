package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.TargetedInterchange;

import java.io.IOException;

public class TargetedInterchangeMarshaller implements Marshaller<TargetedInterchange> {

    @Getter
    private static final Marshaller<TargetedInterchange> instance = new TargetedInterchangeMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, TargetedInterchange source) throws IOException {

        if (source != null) {
            writer.writeStartObject();

            write(writer, "interchangeCode", source.interchangeCode());

            write(writer, "distributorVehicleJourneyRef", source.distributorVehicleJourneyRef());

            // distributorConnectionLinkRef ;
            DistributorConnectionLinkMarshaller.getInstance().write(writer, source.distributorConnectionLink());

            // distributorVisitNumber :long;
            // distributorOrder :long;
            write(writer, "staySeated", source.staySeated());
            write(writer, "guaranteed", source.guaranteed());

            // advertised :bool;
            // standardWaitTime :long;
            write(writer, "maximumWaitTime", source.maximumWaitTime());

            // maximumAutomaticWaitTime :long;
            // standardTransferTime :long;
            // minimumTransferTime :long;
            // maximumTransferTime :long;

            writer.writeEndObject();
        }
    }

}
