package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

class ViaBuilder {


    @Builder
    public static Document create(String placeRef, String placeName) {

        Document result = new Document("placeRef", placeRef);
        append(result, "placeName", placeName);

        return result;
    }

}
