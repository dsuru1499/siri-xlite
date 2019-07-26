package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import siri_xlite.common.DefaultParameters;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public interface SiriServiceMarshaller<P extends DefaultParameters> {

    public default void writeServiceDeliveryInfo(P parameters, JsonGenerator writer) throws IOException {
        writer.writeStringField("ResponseTimestamp",
                SiriStructureFactory.createXMLGregorianCalendar(parameters.getNow()));
        writer.writeStringField("ProducerRef", SiriStructureFactory.createParticipantRef(parameters.getProducerDomain(),
                parameters.getProducerName()));
        writer.writeStringField("ResponseMessageIdentifier", SiriStructureFactory.createMessageIdentifier());
        String messageIdentifier = parameters.getMessageIdentifier();
        if (messageIdentifier != null && !messageIdentifier.isEmpty()) {
            writer.writeStringField("RequestMessageRef", messageIdentifier);
        }
    }
}
