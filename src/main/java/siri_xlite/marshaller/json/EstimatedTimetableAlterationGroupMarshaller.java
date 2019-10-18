package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

public class EstimatedTimetableAlterationGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new EstimatedTimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set datedDocumentRef
        writeField(writer, "DatedDocumentRef", source.getString("datedDocumentRef"));

        // datedDocumentIndirectRef :string;
        // estimatedDocumentCode :string;

        // set extraJourney
        writeField(writer, "ExtraJourney", source.getBoolean("extraJourney"));

        // set cancellation
        writeField(writer, "Cancellation", source.getString("cancellation"));

    }

}