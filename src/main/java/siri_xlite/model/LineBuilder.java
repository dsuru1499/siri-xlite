package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import java.util.Collection;
import java.util.Date;

import static siri_xlite.common.DocumentUtils.append;

public class LineBuilder {

    @Builder
    public static LineDocument create(Date recordedAtTime, String lineRef, String lineName,
                                      Collection<Document> destinations) {

        LineDocument result = new LineDocument();
        append(result, "recordedAtTime", recordedAtTime);
        append(result, "lineRef", lineRef);
        append(result, "lineName", lineName);
        append(result, "destinations", destinations);

        return result;
    }

}
