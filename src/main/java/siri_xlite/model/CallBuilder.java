package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import java.util.Date;
import java.util.List;

import static siri_xlite.common.DocumentUtils.append;

public class CallBuilder {

    @Builder
    public static Document create(Boolean extraCall, Boolean cancellation, String stopPointRef, Integer order,
                                  String stopPointName, String originDisplay, String destinationDisplay, List<String> situationRefs,
                                  Date aimedArrivalTime, Date actualDepartureTime, Date expectedArrivalTime, Integer arrivalStatus,
                                  String arrivalProximityText, String arrivalPlatformName, Date aimedDepartureTime,
                                  Date expectedDepartureTime, Date actualArrivalTime, Integer departureStatus, String departurePlatformName,
                                  Integer departureBoardingActivity, Long aimedHeadwayInterval, Long expectedHeadwayInterval,
                                  Long distanceFromStop, Long numberOfStopsAway, Boolean vehicleAtStop, Boolean platformTraversal,
                                  List<Document> targetedInterchanges) {

        Document result = new Document();
        append(result, "extraCall", extraCall);
        append(result, "cancellation", cancellation);
        append(result, "stopPointRef", stopPointRef);
        append(result, "order", order);
        append(result, "stopPointName", stopPointName);
        append(result, "originDisplay", originDisplay);
        append(result, "destinationDisplay", destinationDisplay);
        append(result, "situationRefs", situationRefs);
        append(result, "aimedArrivalTime", aimedArrivalTime);
        append(result, "actualDepartureTime", actualDepartureTime);
        append(result, "expectedArrivalTime", expectedArrivalTime);
        append(result, "arrivalStatus", arrivalStatus);
        append(result, "arrivalProximityText", arrivalProximityText);
        append(result, "arrivalPlatformName", arrivalPlatformName);
        append(result, "aimedDepartureTime", aimedDepartureTime);
        append(result, "expectedDepartureTime", expectedDepartureTime);
        append(result, "actualArrivalTime", actualArrivalTime);
        append(result, "departureStatus", departureStatus);
        append(result, "departurePlatformName", departurePlatformName);
        append(result, "departureBoardingActivity", departureBoardingActivity);
        append(result, "aimedHeadwayInterval", aimedHeadwayInterval);
        append(result, "expectedHeadwayInterval", expectedHeadwayInterval);
        append(result, "distanceFromStop", distanceFromStop);
        append(result, "numberOfStopsAway", numberOfStopsAway);
        append(result, "vehicleAtStop", vehicleAtStop);
        append(result, "platformTraversal", platformTraversal);
        append(result, "targetedInterchanges", targetedInterchanges);

        return result;
    }

}
