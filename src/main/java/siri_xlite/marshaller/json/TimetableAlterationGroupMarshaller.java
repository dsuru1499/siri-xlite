package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.time.LocalDateTime;

public class TimetableAlterationGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new TimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {
        // framedDocumentRef
        writeObject(writer, "FramedDocumentRef", source.getString("datedDocumentRef"), datedDocumentRef -> {
            writeField(writer, "DatedDocumentRef", datedDocumentRef);
            writeField(writer, "DataFrameRef", LocalDateTime.now());
        });

        // DocumentRef :string;

        // set extraJourney
        writeField(writer, "ExtraJourney", source.getBoolean("extraJourney"));

        // set cancellation
        writeField(writer, "Cancellation", source.getBoolean("cancellation"));

    }

}
