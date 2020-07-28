package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import siri_xlite.common.Parameters;

public interface Marshaller<T> {

    <P extends Parameters> void write(JsonGenerator writer, T source);

}
