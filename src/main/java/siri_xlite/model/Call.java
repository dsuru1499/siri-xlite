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
class Call {
    // EstimatedCallStructure
    Boolean extraCall;
    Boolean cancellation;

    // StopPointInSequenceGroup
    String stopPointRef;
    // visitNumber :ushort;
    Integer order;
    String stopPointName;

    // CallRealTimeInfoGroup
    // predictionInaccurate :bool;
    // occupancy :byte;

    // CallPropertyGroup
    // timingPoint :bool;
    // boardingStretch :bool;
    // requestStop :bool;
    String originDisplay;
    String destinationDisplay;

    // CallNoteGroup
    // callNote :[string];

    // DisruptionGroup
    // facilityConditionElement: [FacilityCondition];
    // facilityChangeElement :FacilityChange;
    @Singular
    List<String> situationRefs;

    // OnwardVehicleArrivalTimesGroup VehicleArrivalTimesGroup, MonitoredCallArrivalTimesGroup
    Date aimedArrivalTime;
    Date actualDepartureTime;
    Date expectedArrivalTime;
    // expectedArrivalPredictionQuality :PredictionQuality;
    // latestExpectedArrivalTime :long;

    // MonitoredStopArrivalStatusGroup
    Integer arrivalStatus;
    String arrivalProximityText;
    String arrivalPlatformName;
    // arrivalBoardingActivity :byte;
    // arrivalStopAssignment :StopAssignment;
    // arrivalOperatorRefs :[string];

    // OnwardVehicleDepartureTimesGroup VehicleDepartureTimesGroup, MonitoredCallDepartureTimesGroup
    Date aimedDepartureTime;
    Date expectedDepartureTime;
    // provisionalExpectedDepartureTime :long;
    // earliestExpectedDepartureTime :long;
    // expectedDeparturePredictionQuality :PredictionQuality;
    Date actualArrivalTime;

    // PassengerDepartureTimesGroup
    // aimedLatestPassengerAccessTime :long;
    // expectedLatestPassengerAccessTime :long;

    // MonitoredStopDepartureStatusGroup
    Integer departureStatus;
    // departureProximityText :string;
    String departurePlatformName;
    Integer departureBoardingActivity;
    // departureStopAssignment :StopAssignment;
    // departureOperatorRefs :[string];

    // HeadwayIntervalGroup
    Long aimedHeadwayInterval;
    Long expectedHeadwayInterval;

    // StopProximityGroup
    Long distanceFromStop;
    Long numberOfStopsAway;

    // MonitoredCallStructure

    // CallRealtimeGroup
    Boolean vehicleAtStop;
    // vehicleLocationAtStop : Location;

    // CallRailGroup
    // reversesAtStop :bool;
    Boolean platformTraversal;
    // signalStatus :string;

    // DatedCallStructure
    @Singular
    List<TargetedInterchange> targetedInterchanges;
    // fromServiceJourneyInterchange :[ServiceJourneyInterchange];
    // toServiceJourneyInterchange :[ServiceJourneyInterchange];
}
