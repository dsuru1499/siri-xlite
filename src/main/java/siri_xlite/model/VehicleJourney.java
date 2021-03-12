package siri_xlite.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
class VehicleJourney {
    // EstimatedVehicleJourney
    Date recordedAtTime;

    // LineIdentityGroup
    String lineRef;
    String directionRef;

    // framedVehicleJourneyRef :string;
    // datedVehicleJourneyCode :string;

    // EstimatedTimetableAlterationGroup
    String datedVehicleJourneyRef;
    // datedVehicleJourneyIndirectRef :string;
    // estimatedVehicleJourneyCode :string;
    Boolean extraJourney;
    Boolean cancellation;

    // JourneyPatternInfoGroup
    String journeyPatternRef;
    String journeyPatternName;
    @Singular
    List<Integer> vehicleModes;
    String routeRef;
    String publishedLineName;
    // groupOfLinesRef :string;
    String directionName;
    // externalLineRef :string;

    // VehicleJourneyInfoGroup
    String originRef;
    String originName;
    // originShortName :string;
    // destinationDisplayAtOrigin :string;
    @Singular
    List<Via> vias;
    String destinationRef;
    String destinationName;
    // destinationShortName :string;
    // originDisplayAtDestination :string;
    String operatorRef;
    String productCategoryRef;
    @Singular
    List<String> serviceFeatureRefs;
    @Singular
    List<String> vehicleFeatureRefs;
    String vehicleJourneyName;
    @Singular
    List<String> journeyNotes;
    // publicContact :SimpleContact;
    // operationsContact:SimpleContact;
    Boolean headwayService;
    Date originAimedDepartureTime;
    Date destinationAimedArrivalTime;
    Integer firstOrLastJourney;

    // DisruptionGroup
    // ? facilityConditionElement :[FacilityCondition];
    // facilityChangeElement :[FacilityChange];
    @Singular
    List<String> situationRefs;

    // JourneyProgressGroup
    Boolean monitored;
    String monitoringError;
    Boolean inCongestion;
    Boolean inPanic;
    // predictionInaccurate :bool;
    // dataSource :string;
    // confidenceLevel :string;
    Location vehicleLocation;
    // ? locationRecordedAtTime :long;
    Double bearing;
    // progressRate :string;
    // ? velocity : long;
    // ? engineOn :bool;
    Integer occupancy;
    Long delay;
    // progressStatus :[string];
    // vehicleStatus : string;

    // TrainOperationalInfoGroup
    // trainBlockPart : [TrainBlockPart];
    // blockRef :string;
    // courseOfJourneyRef :string;
    // vehicleJourneyRef :string;
    // vehicleRef :string;
    // additionalVehicleJourneyRef :[string];
    // driverRef :string;
    // driverName :string;
    @Singular
    List<String> trainNumbers;
    @Singular
    List<JourneyPart> journeyParts;

    // isCompleteStopSequence :bool;

    // MonitoredVehicleJourney

    // DatedVehicleJourney

    String originDisplay;
    String destinationDisplay;
    // lineNote :[string];

    @Singular
    List<Call> calls;
}