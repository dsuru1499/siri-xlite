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
final class Call {
    // EstimatedCallStructure
    private Boolean extraCall;
    private Boolean cancellation;

    // StopPointInSequenceGroup
    private String stopPointRef;
    // visitNumber :ushort;
    private Integer order;
    private String stopPointName;

    // CallRealTimeInfoGroup
    // predictionInaccurate :bool;
    // occupancy :byte;

    // CallPropertyGroup
    // timingPoint :bool;
    // boardingStretch :bool;
    // requestStop :bool;
    private String originDisplay;
    private String destinationDisplay;

    // CallNoteGroup
    // callNote :[string];

    // DisruptionGroup
    // facilityConditionElement: [FacilityCondition];
    // facilityChangeElement :FacilityChange;
    @Singular
    private List<String> situationRefs;

    // OnwardVehicleArrivalTimesGroup VehicleArrivalTimesGroup, MonitoredCallArrivalTimesGroup
    private Date aimedArrivalTime;
    private Date actualDepartureTime;
    private Date expectedArrivalTime;
    // expectedArrivalPredictionQuality :PredictionQuality;
    // latestExpectedArrivalTime :long;

    // MonitoredStopArrivalStatusGroup
    private Integer arrivalStatus;
    private String arrivalProximityText;
    private String arrivalPlatformName;
    // arrivalBoardingActivity :byte;
    // arrivalStopAssignment :StopAssignment;
    // arrivalOperatorRefs :[string];

    // OnwardVehicleDepartureTimesGroup VehicleDepartureTimesGroup, MonitoredCallDepartureTimesGroup
    private Date aimedDepartureTime;
    private Date expectedDepartureTime;
    // provisionalExpectedDepartureTime :long;
    // earliestExpectedDepartureTime :long;
    // expectedDeparturePredictionQuality :PredictionQuality;
    private Date actualArrivalTime;

    // PassengerDepartureTimesGroup
    // aimedLatestPassengerAccessTime :long;
    // expectedLatestPassengerAccessTime :long;

    // MonitoredStopDepartureStatusGroup
    private Integer departureStatus;
    // departureProximityText :string;
    private String departurePlatformName;
    private Integer departureBoardingActivity;
    // departureStopAssignment :StopAssignment;
    // departureOperatorRefs :[string];

    // HeadwayIntervalGroup
    private Long aimedHeadwayInterval;
    private Long expectedHeadwayInterval;

    // StopProximityGroup
    private Long distanceFromStop;
    private Long numberOfStopsAway;

    // MonitoredCallStructure

    // CallRealtimeGroup
    private Boolean vehicleAtStop;
    // vehicleLocationAtStop : Location;

    // CallRailGroup
    // reversesAtStop :bool;
    private Boolean platformTraversal;
    // signalStatus :string;

    // DatedCallStructure
    @Singular
    List<TargetedInterchange> targetedInterchanges;
    // fromServiceJourneyInterchange :[ServiceJourneyInterchange];
    // toServiceJourneyInterchange :[ServiceJourneyInterchange];
}
