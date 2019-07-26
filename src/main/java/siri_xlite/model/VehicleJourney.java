package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
public final class VehicleJourney {
    // EstimatedVehicleJourney
    private Long recordedAtTime;

    // LineIdentityGroup
    private String lineRef;
    private String directionRef;

    // framedVehicleJourneyRef :string;
    // datedVehicleJourneyCode :string;

    // EstimatedTimetableAlterationGroup
    private String datedVehicleJourneyRef;
    // datedVehicleJourneyIndirectRef :string;
    // estimatedVehicleJourneyCode :string;
    private Boolean extraJourney;
    private Boolean cancellation;

    // JourneyPatternInfoGroup
    private String journeyPatternRef;
    private String journeyPatternName;
    @Singular
    private List<Integer> vehicleModes;
    private String routeRef;
    private String publishedLineName;
    // groupOfLinesRef :string;
    private String directionName;
    // externalLineRef :string;

    // VehicleJourneyInfoGroup
    private String originRef;
    private String originName;
    // originShortName :string;
    // destinationDisplayAtOrigin :string;
    @Singular
    private List<Via> vias;
    private String destinationRef;
    private String destinationName;
    // destinationShortName :string;
    // originDisplayAtDestination :string;
    private String operatorRef;
    private String productCategoryRef;
    @Singular
    private List<String> serviceFeatureRefs;
    @Singular
    private List<String> vehicleFeatureRefs;
    private String vehicleJourneyName;
    @Singular
    private List<String> journeyNotes;
    // publicContact :SimpleContact;
    // operationsContact:SimpleContact;
    private Boolean headwayService;
    private Long originAimedDepartureTime;
    private Long destinationAimedArrivalTime;
    private Integer firstOrLastJourney;
    // DisruptionGroup
    // ? facilityConditionElement :[FacilityCondition];
    // facilityChangeElement :[FacilityChange];
    @Singular
    private List<String> situationRefs;

    // JourneyProgressGroup
    private Boolean monitored;
    private String monitoringError;
    private Boolean inCongestion;
    private Boolean inPanic;
    // predictionInaccurate :bool;
    // dataSource :string;
    // confidenceLevel :string;
    private Location vehicleLocation;
    // ? locationRecordedAtTime :long;
    private Double bearing;
    // progressRate :string;
    // ? velocity : long;
    // ? engineOn :bool;
    private Integer occupancy;
    private Long delay;
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
    private List<String> trainNumbers;
    @Singular
    private List<JourneyPart> journeyParts;

    // isCompleteStopSequence :bool;

    // MonitoredVehicleJourney

    // DatedVehicleJourney

    private String originDisplay;
    private String destinationDisplay;
    // lineNote :[string];

    @Singular
    List<Call> calls;
}