package siri_xlite.parser.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import siri_xlite.common.JsonUtils;
import siri_xlite.common.Parameters;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;

@Slf4j
public class DestinationParser implements Parser, JsonUtils {

    @Getter
    private static final Parser instance = new DestinationParser();

    @Override
    public <P extends Parameters> void parse(JsonParser reader, JsonGenerator writer, P parameters) throws IOException {
        writer.writeStartObject();
        while (reader.nextToken() != END_OBJECT) {
            String name = reader.getCurrentName();
            if ("destinationRef".equals(name)) {
                write(writer, "DestinationRef", reader.nextTextValue());
            } else if ("placeName".equals(name)) {
                write(writer, "PlaceName", reader.nextTextValue());
            }
        }
        writer.writeEndObject();
    }
}
