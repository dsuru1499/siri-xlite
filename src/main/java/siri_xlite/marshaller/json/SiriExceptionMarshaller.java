package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import siri_xlite.common.JsonUtils;
import siri_xlite.service.common.SiriException;

import java.util.Collection;
import java.util.Map;

public class SiriExceptionMarshaller implements Marshaller<SiriException>, JsonUtils {

    @Getter
    private static final Marshaller<SiriException> instance = new SiriExceptionMarshaller();

    @Override
    public void write(JsonGenerator writer, SiriException e) {

        switch (e.getCode()) {
        case UnapprovedKeyAccessError:
        case UnknownParticipantError:
        case UnknownEndpointError:
        case EndpointDeniedAccessError:
        case EndpointNotAvailableAccessError:
        case ServiceNotAvailableError:
        case CapabilityNotSupportedError:
        case AccessNotAllowedError:
        case InvalidDataReferencesError:
        case BeyondDataHorizon:
        case NoInfoForTopicError:
        case ParametersIgnoredError:
        case UnknownExtensionsError:
        case AllowedResourceUsageExceededError:
        case OtherError: {

            write(writer, e.getCode().name(), () -> {
                write(writer, "ErrorText", ExceptionUtils.getMessage(e));
                Map<String, Object> values = e.getValues();
                if (values != null) {
                    for (Map.Entry<String, Object> entry : values.entrySet()) {
                        if (entry.getValue() instanceof String) {
                            String value = (String) entry.getValue();
                            write(writer, entry.getKey(), value);
                        } else if (entry.getValue() instanceof Collection) {
                            Collection<?> list = (Collection<?>) entry.getValue();
                            for (Object o : list) {
                                if (o instanceof String) {
                                    String value = (String) o;
                                    write(writer, entry.getKey(), value);
                                }
                            }
                        }
                    }
                }
            });
            break;
        }
        default:
            break;
        }
    }

}
