package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.time.LocalDateTime;

public class TimetableAlterationGroupMarshaller implements Marshaller<Document> {

    private static final String EXTRA_JOURNEY = "extraJourney";
    private static final String CANCELLATION = "cancellation";
    private static final String DATED_DOCUMENT_REF = "datedDocumentRef";
    private static final String FRAMED_DOCUMENT_REF = "framedDocumentRef";
    private static final String DATA_FRAME_REF = "dataFrameRef";
    @Getter
    private static final Marshaller<Document> instance = new TimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {
        // framedDocumentRef
        writeObject(writer, FRAMED_DOCUMENT_REF, source.getString(DATED_DOCUMENT_REF), datedDocumentRef -> {
            writeField(writer, DATED_DOCUMENT_REF, datedDocumentRef);
            writeField(writer, DATA_FRAME_REF, LocalDateTime.now());
        });

        // DocumentRef :string;

        // set extraJourney
        writeField(writer, EXTRA_JOURNEY, source.getBoolean(EXTRA_JOURNEY));

        // set cancellation
        writeField(writer, CANCELLATION, source.getBoolean(CANCELLATION));

    }

}
