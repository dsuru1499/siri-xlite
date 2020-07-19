package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import java.util.Collection;
import java.util.Date;

import static siri_xlite.common.DocumentUtils.append;

public class StopPointBuilder {

    @Builder
    public static StopPointDocument create(Date recordedAtTime, String stopPointRef, String parent, String stopName,
                                           Document location, Collection<String> lineRefs) {

        StopPointDocument result = new StopPointDocument();
        append(result, "recordedAtTime", recordedAtTime);
        append(result, "stopPointRef", stopPointRef);
        append(result, "parent", parent);
        append(result, "stopName", stopName);
        append(result, "lineRefs", lineRefs);
        append(result, "location", location);

        return result;
    }

}
