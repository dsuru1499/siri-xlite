package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import siri_xlite.common.JsonUtils;
import siri_xlite.service.common.Parameters;

public interface Marshaller<T> extends JsonUtils {

    <P extends Parameters> void write(JsonGenerator writer, T source);

}
