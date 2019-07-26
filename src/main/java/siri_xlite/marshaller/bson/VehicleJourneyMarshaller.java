package siri_xlite.marshaller.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.common.Parameters;
import siri_xlite.marshaller.json.Marshaller;
import siri_xlite.model.Call;
import siri_xlite.model.JourneyPart;
import siri_xlite.model.VehicleJourney;
import siri_xlite.model.Via;

import java.io.IOException;

public class VehicleJourneyMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new VehicleJourneyMarshaller();

    @Override
    public <P extends Parameters> void write(JsonGenerator writer, VehicleJourney source) throws IOException {
        if (source != null) {
            writer.writeStartObject();

            // EstimatedVehicleJourney
            write(writer, "recordedAtTime", source.recordedAtTime());

            // LineIdentityGroup
            write(writer, "lineRef", source.lineRef());
            write(writer, "directionRef", source.directionRef());

            // framedVehicleJourneyRef :string;
            // datedVehicleJourneyCode :string;

            // EstimatedTimetableAlterationGroup
            write(writer, "datedVehicleJourneyRef", source.datedVehicleJourneyRef());
            // datedVehicleJourneyIndirectRef :string;
            // estimatedVehicleJourneyCode :string;
            write(writer, "extraJourney", source.extraJourney());
            write(writer, "cancellation", source.cancellation());

            // JourneyPatternInfoGroup
            write(writer, "journeyPatternRef", source.journeyPatternRef());
            write(writer, "journeyPatternName", source.journeyPatternName());
            write(writer, "vehicleMode", source.vehicleModes());
            write(writer, "routeRef", source.routeRef());
            write(writer, "publishedLineName", source.routeRef());
            // groupOfLinesRef :string;
            write(writer, "directionName", source.directionName());
            // externalLineRef :string;

            // VehicleJourneyInfoGroup
            write(writer, "originRef", source.originRef());
            write(writer, "originName", source.originName());
            // originShortName :string;
            // destinationDisplayAtOrigin :string;
            write(writer, "vias", source.vias(), wrapper((Via t) -> ViaMarshaller.getInstance().write(writer, t)));
            write(writer, "destinationRef", source.destinationRef());
            write(writer, "destinationName", source.destinationName());
            // destinationShortName :string;
            // originDisplayAtDestination :string;
            write(writer, "operatorRef", source.operatorRef());
            write(writer, "productCategoryRef", source.productCategoryRef());
            write(writer, "serviceFeatureRef", source.serviceFeatureRefs());
            write(writer, "vehicleFeatureRef", source.vehicleFeatureRefs());
            write(writer, "vehicleJourneyName", source.vehicleJourneyName());
            write(writer, "journeyNote", source.journeyNotes());
            // publicContact :SimpleContact;
            // operationsContact:SimpleContact;
            write(writer, "headwayService", source.headwayService());
            write(writer, "originAimedDepartureTime", source.originAimedDepartureTime());
            write(writer, "destinationAimedArrivalTime", source.destinationAimedArrivalTime());
            write(writer, "firstOrLastJourney", source.firstOrLastJourney());
            // DisruptionGroup
            // ? facilityConditionElement :[FacilityCondition];
            // facilityChangeElement :[FacilityChange];
            write(writer, "situationRefs", source.situationRefs());

            // JourneyProgressGroup
            write(writer, "monitored", source.monitored());
            write(writer, "monitoringError", source.monitoringError());
            write(writer, "inCongestion", source.inCongestion());
            write(writer, "inPanic", source.inPanic());
            // predictionInaccurate :bool;
            // dataSource :string;
            // confidenceLevel :string;
            LocationMarshaller.getInstance().write(writer, source.vehicleLocation());
            // ? locationRecordedAtTime :long;
            write(writer, "bearing", source.bearing());
            // progressRate :string;
            // ? velocity : long;
            // ? engineOn :bool;
            write(writer, "occupancy", source.occupancy());
            write(writer, "delay", source.delay());
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
            write(writer, "trainNumbers", source.trainNumbers());
            write(writer, "journeyParts", source.journeyParts(),
                    wrapper((JourneyPart t) -> JourneyPartMarshaller.getInstance().write(writer, t)));

            // isCompleteStopSequence :bool;

            // MonitoredVehicleJourney

            // DatedVehicleJourney

            write(writer, "originDisplay", source.originDisplay());
            write(writer, "destinationDisplay", source.destinationDisplay());
            // lineNote :[string];

            write(writer, "calls", source.calls(), wrapper((Call t) -> CallMarshaller.getInstance().write(writer, t)));

            writer.writeEndObject();
        }
    }
}
