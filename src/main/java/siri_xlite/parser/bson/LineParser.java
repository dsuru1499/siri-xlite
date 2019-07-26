package siri_xlite.parser.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import siri_xlite.common.JsonUtils;
import siri_xlite.common.Parameters;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;

@Slf4j
public class LineParser implements Parser, JsonUtils {

    @Getter
    private static final Parser instance = new LineParser();

    @Override
    public <P extends Parameters> void parse(JsonParser reader, JsonGenerator writer, P parameters) throws IOException {

        writer.writeStartObject();
        while (reader.nextToken() != END_OBJECT) {
            String name = reader.getCurrentName();
            if ("lineRef".equals(name)) {
                write(writer, "lineRef", reader.nextTextValue());
            } else if ("lineName".equals(name)) {
                write(writer, "lineName", reader.nextTextValue());
            } else if ("destinations".equals(name)) {
                writer.writeArrayFieldStart("Destinations");
                while (reader.nextToken() != END_ARRAY) {
                    DestinationParser.getInstance().parse(reader, writer, parameters);
                }
                writer.writeEndArray();
            }
        }

        writer.writeEndObject();
    }

}
