package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

class DistributorConnectionLinkBuilder {

    @Builder
    public static Document create(String connectionLinkCode, String stopPointRef, String stopPointName,
            Long defaultDuration, Long frequentTravellerDuration, Long occasionalTravellerDuration,
            Long impairedAccessDuration) {

        Document result = new Document("connectionLinkCode", connectionLinkCode);
        append(result, "stopPointRef", stopPointRef);
        append(result, "stopPointName", stopPointName);
        append(result, "defaultDuration", defaultDuration);
        append(result, "frequentTravellerDuration", frequentTravellerDuration);
        append(result, "occasionalTravellerDuration", occasionalTravellerDuration);
        append(result, "impairedAccessDuration", impairedAccessDuration);

        return result;
    }

}
