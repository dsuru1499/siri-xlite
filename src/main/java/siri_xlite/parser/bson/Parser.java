package siri_xlite.parser.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonParser;
import org.bson.RawBsonDocument;
import siri_xlite.common.Parameters;

import java.io.IOException;

public interface Parser {

    BsonFactory factory = new BsonFactory().enable(BsonParser.Feature.HONOR_DOCUMENT_LENGTH);

    default <P extends Parameters> void parse(RawBsonDocument document, JsonGenerator writer, P parameters)
            throws IOException {
        byte[] buffer = document.getByteBuffer().array();
        JsonParser reader = factory.createParser(buffer);
        parse(reader, writer, parameters);
    }

    <P extends Parameters> void parse(JsonParser reader, JsonGenerator writer, P parameters) throws IOException;
}
