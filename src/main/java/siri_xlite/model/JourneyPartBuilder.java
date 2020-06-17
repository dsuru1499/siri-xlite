package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

class JourneyPartBuilder {

    @Builder
    public static Document create(String journeyPartRef, String trainNumberRef) {

        Document result = new Document("journeyPartRef", journeyPartRef);
        append(result, "trainNumberRef", trainNumberRef);

        return result;
    }

}
