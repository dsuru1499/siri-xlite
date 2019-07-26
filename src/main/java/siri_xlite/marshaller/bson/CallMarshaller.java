package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.Call;
import siri_xlite.model.TargetedInterchange;

import java.io.IOException;

public class CallMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new CallMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) throws IOException {

        if (source != null) {
            writer.writeStartObject();

            // EstimatedCallStructure

            write(writer, "extraCall", source.extraCall());
            write(writer, "cancellation", source.cancellation());

            // StopPointInSequenceGroup
            write(writer, "stopPointRef", source.stopPointRef());

            // visitNumber :ushort;
            write(writer, "order", source.order());
            write(writer, "stopPointName", source.stopPointName());

            // CallRealTimeInfoGroup
            // predictionInaccurate :bool;
            // occupancy :byte;

            // CallPropertyGroup
            // timingPoint :bool;
            // boardingStretch :bool;
            // requestStop :bool;
            write(writer, "originDisplay", source.originDisplay());
            write(writer, "destinationDisplay", source.destinationDisplay());

            // CallNoteGroup
            // callNote :[string];

            // DisruptionGroup
            // facilityConditionElement: [FacilityCondition];
            // facilityChangeElement :FacilityChange;

            write(writer, "situationRefs", source.situationRefs());

            // OnwardVehicleArrivalTimesGroup VehicleArrivalTimesGroup, MonitoredCallArrivalTimesGroup
            write(writer, "aimedArrivalTime", source.aimedArrivalTime());
            write(writer, "actualDepartureTime", source.actualDepartureTime());
            write(writer, "expectedArrivalTime", source.expectedArrivalTime());

            // expectedArrivalPredictionQuality :PredictionQuality;
            // latestExpectedArrivalTime :long;

            // MonitoredStopArrivalStatusGroup
            write(writer, "arrivalStatus", source.arrivalStatus());
            write(writer, "arrivalProximityText", source.arrivalProximityText());
            write(writer, "arrivalPlatformName", source.arrivalPlatformName());

            // arrivalBoardingActivity :byte;
            // arrivalStopAssignment :StopAssignment;
            // arrivalOperatorRefs :[string];

            // OnwardVehicleDepartureTimesGroup VehicleDepartureTimesGroup, MonitoredCallDepartureTimesGroup
            write(writer, "aimedDepartureTime", source.aimedDepartureTime());
            write(writer, "expectedDepartureTime", source.expectedDepartureTime());

            // provisionalExpectedDepartureTime :long;
            // earliestExpectedDepartureTime :long;
            // expectedDeparturePredictionQuality :PredictionQuality;
            write(writer, "actualArrivalTime", source.actualArrivalTime());

            // PassengerDepartureTimesGroup
            // aimedLatestPassengerAccessTime :long;
            // expectedLatestPassengerAccessTime :long;

            // MonitoredStopDepartureStatusGroup
            write(writer, "departureStatus", source.departureStatus());
            // departureProximityText :string;
            write(writer, "departurePlatformName", source.departurePlatformName());
            write(writer, "departureBoardingActivity", source.departureBoardingActivity());
            // departureStopAssignment :StopAssignment;
            // departureOperatorRefs :[string];

            // HeadwayIntervalGroup
            write(writer, "aimedHeadwayInterval", source.aimedHeadwayInterval());
            write(writer, "expectedHeadwayInterval", source.expectedHeadwayInterval());

            // StopProximityGroup
            write(writer, "distanceFromStop", source.distanceFromStop());
            write(writer, "numberOfStopsAway", source.numberOfStopsAway());

            // MonitoredCallStructure

            // CallRealtimeGroup
            write(writer, "vehicleAtStop", source.vehicleAtStop());

            // vehicleLocationAtStop : Location;

            // CallRailGroup
            // reversesAtStop :bool;
            write(writer, "platformTraversal", source.platformTraversal());

            // signalStatus :string;

            // DatedCallStructure
            write(writer, "targetedInterchange", source.targetedInterchanges(),
                    wrapper((TargetedInterchange t) -> TargetedInterchangeMarshaller.getInstance().write(writer, t)));

            // fromServiceJourneyInterchange :[ServiceJourneyInterchange];
            // toServiceJourneyInterchange :[ServiceJourneyInterchange];

            writer.writeEndObject();
        }

    }
}
