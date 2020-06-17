package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

class StopAssignmentBuilder {

    @Builder
    public static Document create(String aimedQuayRef, String aimedQuayName, String expectedQuayRef,
            String actualQuayRef) {

        Document result = new Document("aimedQuayRef", aimedQuayRef);
        append(result, "aimedQuayName", aimedQuayName);
        append(result, "expectedQuayRef", expectedQuayRef);
        append(result, "actualQuayRef", actualQuayRef);

        return result;
    }

}
