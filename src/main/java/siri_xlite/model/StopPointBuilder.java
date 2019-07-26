package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import java.util.Collection;

import static siri_xlite.common.DocumentUtils.append;

public class StopPointBuilder {

    @Builder
    public static Document create(String stopPointRef, String parent, String stopName, Document location,
            Collection<String> lineRefs) {

        Document result = new Document("_id", stopPointRef);
        append(result, "stopPointRef", stopPointRef);
        append(result, "_parent", parent);
        append(result, "stopName", stopName);
        append(result, "lineRefs", lineRefs);
        append(result, "location", location);

        return result;
    }

}
