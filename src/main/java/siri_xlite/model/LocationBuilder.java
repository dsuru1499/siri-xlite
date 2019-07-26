package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

public class LocationBuilder {

    @Builder
    public static Document create(Double longitude, Double latitude) {

        Document result = new Document("longitude", longitude);
        append(result, "latitude", latitude);

        return result;
    }

}
