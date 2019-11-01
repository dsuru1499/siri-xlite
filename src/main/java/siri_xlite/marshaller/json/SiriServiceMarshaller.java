package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import siri_xlite.Configuration;
import siri_xlite.common.JsonUtils;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public interface SiriServiceMarshaller<P extends DefaultParameters> extends JsonUtils {

    String INDEX = "index";

    String SERVICE_DELIVERY = "serviceDelivery";
    String RESPONSE_TIMESTAMP = "responseTimestamp";
    String PRODUCER_REF = "ProducerRef";
    String RESPONSE_MESSAGE_IDENTIFIER = "responseMessageIdentifier";
    String REQUEST_MESSAGE_REF = "requestMessageRef";

    default void writeStartServiceDelivery(Configuration configuration, P parameters, JsonGenerator writer) {
        try {
            writer.writeObjectFieldStart(SERVICE_DELIVERY);
            writeField(writer, RESPONSE_TIMESTAMP,
                    SiriStructureFactory.createXMLGregorianCalendar(parameters.getNow()));
            writeField(writer, PRODUCER_REF, SiriStructureFactory
                    .createParticipantRef(configuration.getProducerDomain(), configuration.getProducerName()));
            writeField(writer, RESPONSE_MESSAGE_IDENTIFIER, SiriStructureFactory.createMessageIdentifier());
            String messageIdentifier = parameters.getMessageIdentifier();
            if (messageIdentifier != null && !messageIdentifier.isEmpty()) {
                writeField(writer, REQUEST_MESSAGE_REF, messageIdentifier);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }
}
