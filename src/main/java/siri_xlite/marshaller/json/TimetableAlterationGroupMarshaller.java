package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;

import java.time.LocalDateTime;

import static siri_xlite.common.JsonUtils.*;

public class TimetableAlterationGroupMarshaller implements Marshaller<Document> {

    public static final String EXTRA_JOURNEY = "extraJourney";
    public static final String CANCELLATION = "cancellation";
    public static final String DATED_DOCUMENT_REF = "datedDocumentRef";
    public static final String FRAMED_DOCUMENT_REF = "framedDocumentRef";
    public static final String DATA_FRAME_REF = "dataFrameRef";

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
        writeBooleanField(writer, EXTRA_JOURNEY, source);

        // set cancellation
        writeBooleanField(writer, CANCELLATION, source);

    }

}
