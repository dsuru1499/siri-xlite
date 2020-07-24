package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
class TargetedInterchange {
    String interchangeCode;
    String distributorVehicleJourneyRef;
    // distributorConnectionLinkRef ;
    DistributorConnectionLink distributorConnectionLink;
    // distributorVisitNumber :long;
    // distributorOrder :long;
    Boolean staySeated;
    Boolean guaranteed;
    // advertised :bool;
    // standardWaitTime :long;
    Long maximumWaitTime;
    // maximumAutomaticWaitTime :long;
    // standardTransferTime :long;
    // minimumTransferTime :long;
    // maximumTransferTime :long;
}
