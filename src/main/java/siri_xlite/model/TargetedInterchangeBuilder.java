package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import static siri_xlite.common.DocumentUtils.append;

public class TargetedInterchangeBuilder {

    @Builder
    public static Document create(String interchangeCode, String distributorVehicleJourneyRef,
                                  DistributorConnectionLink distributorConnectionLink, Boolean staySeated, Boolean guaranteed,
                                  Long maximumWaitTime) {

        Document result = new Document("interchangeCode", interchangeCode);
        append(result, "distributorVehicleJourneyRef", distributorVehicleJourneyRef);
        append(result, "distributorConnectionLink", distributorConnectionLink);
        append(result, "staySeated", staySeated);
        append(result, "guaranteed", guaranteed);
        append(result, "maximumWaitTime", maximumWaitTime);

        return result;
    }

}
