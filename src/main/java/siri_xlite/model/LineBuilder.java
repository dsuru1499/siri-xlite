package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import java.util.Collection;

import static siri_xlite.common.DocumentUtils.append;

public class LineBuilder {

    @Builder
    public static LineDocument create(String lineRef, String lineName, Collection<Document> destinations) {

        LineDocument result = new LineDocument("_id", lineRef);
        append(result, "lineRef", lineRef);
        append(result, "lineName", lineName);
        append(result, "destinations", destinations);

        return result;
    }

}
