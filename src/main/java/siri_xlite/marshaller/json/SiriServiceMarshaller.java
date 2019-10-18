package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import siri_xlite.Configuration;
import siri_xlite.common.JsonUtils;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriStructureFactory;

import java.io.IOException;

public interface SiriServiceMarshaller<P extends DefaultParameters> extends JsonUtils {

    default void writeStartDocument(JsonGenerator writer) {
        try {
            writer.writeStartObject();
            writer.writeObjectFieldStart("Siri");
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void writeEndDocument(JsonGenerator writer) {
        try {
            writer.writeEndObject();
            writer.writeEndObject();
            writer.flush();
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void writeStartServiceDelivery(Configuration configuration, P parameters, JsonGenerator writer) {
        try {
            writer.writeObjectFieldStart("ServiceDelivery");
            writeField(writer, "ResponseTimestamp",
                    SiriStructureFactory.createXMLGregorianCalendar(parameters.getNow()));
            writeField(writer, "ProducerRef", SiriStructureFactory
                    .createParticipantRef(configuration.getProducerDomain(), configuration.getProducerName()));
            writeField(writer, "ResponseMessageIdentifier", SiriStructureFactory.createMessageIdentifier());
            String messageIdentifier = parameters.getMessageIdentifier();
            if (messageIdentifier != null && !messageIdentifier.isEmpty()) {
                writeField(writer, "RequestMessageRef", messageIdentifier);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }
}
