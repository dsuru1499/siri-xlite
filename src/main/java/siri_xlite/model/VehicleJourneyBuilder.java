package siri_xlite.model;

import lombok.Builder;
import org.bson.Document;

import java.util.Date;
import java.util.List;

import static siri_xlite.common.DocumentUtils.append;

public class VehicleJourneyBuilder {

    @Builder
    public static VehicleJourneyDocument create(Date recordedAtTime, String lineRef, String directionRef,
                                                String datedVehicleJourneyRef, Boolean extraJourney, Boolean cancellation, String journeyPatternRef,
                                                String journeyPatternName, List<Integer> vehicleModes, String routeRef, String publishedLineName,
                                                String directionName, String originRef, String originName, List<Document> vias, String destinationRef,
                                                String destinationName, String operatorRef, String productCategoryRef, List<String> serviceFeatureRefs,
                                                List<String> vehicleFeatureRefs, String vehicleJourneyName, List<String> journeyNotes,
                                                Boolean headwayService, Date originAimedDepartureTime, Date destinationAimedArrivalTime,
                                                Integer firstOrLastJourney, List<String> situationRefs, Boolean monitored, String monitoringError,
                                                Boolean inCongestion, Boolean inPanic, Document vehicleLocation, Double bearing, Integer occupancy,
                                                Long delay, List<String> trainNumbers, List<Document> journeyParts, String originDisplay,
                                                String destinationDisplay, List<Document> calls) {

        VehicleJourneyDocument result = new VehicleJourneyDocument();
        append(result, "recordedAtTime", recordedAtTime);
        append(result, "lineRef", lineRef);
        append(result, "directionRef", directionRef);
        append(result, "datedVehicleJourneyRef", datedVehicleJourneyRef);
        append(result, "extraJourney", extraJourney);
        append(result, "cancellation", cancellation);
        append(result, "journeyPatternRef", journeyPatternRef);
        append(result, "journeyPatternName", journeyPatternName);
        append(result, "vehicleModes", vehicleModes);
        append(result, "routeRef", routeRef);
        append(result, "publishedLineName", publishedLineName);
        append(result, "directionName", directionName);
        append(result, "originRef", originRef);
        append(result, "originName", originName);
        append(result, "vias", vias);
        append(result, "destinationRef", destinationRef);
        append(result, "destinationName", destinationName);
        append(result, "operatorRef", operatorRef);
        append(result, "productCategoryRef", productCategoryRef);
        append(result, "serviceFeatureRefs", serviceFeatureRefs);
        append(result, "vehicleFeatureRefs", vehicleFeatureRefs);
        append(result, "vehicleJourneyName", vehicleJourneyName);
        append(result, "journeyNotes", journeyNotes);
        append(result, "headwayService", headwayService);
        append(result, "originAimedDepartureTime", originAimedDepartureTime);
        append(result, "destinationAimedArrivalTime", destinationAimedArrivalTime);
        append(result, "firstOrLastJourney", firstOrLastJourney);
        append(result, "situationRefs", situationRefs);
        append(result, "monitored", monitored);
        append(result, "monitoringError", monitoringError);
        append(result, "inCongestion", inCongestion);
        append(result, "inPanic", inPanic);
        append(result, "vehicleLocation", vehicleLocation);
        append(result, "bearing", bearing);
        append(result, "occupancy", occupancy);
        append(result, "delay", delay);
        append(result, "trainNumbers", trainNumbers);
        append(result, "journeyParts", journeyParts);
        append(result, "originDisplay", originDisplay);
        append(result, "destinationDisplay", destinationDisplay);
        append(result, "calls", calls);
        return result;
    }

}
