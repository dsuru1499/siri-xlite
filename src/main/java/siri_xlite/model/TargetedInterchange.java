package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public final class TargetedInterchange {
    private String interchangeCode;
    private String distributorVehicleJourneyRef;
    // distributorConnectionLinkRef ;
    private DistributorConnectionLink distributorConnectionLink;
    // distributorVisitNumber :long;
    // distributorOrder :long;
    private Boolean staySeated;
    private Boolean guaranteed;
    // advertised :bool;
    // standardWaitTime :long;
    private Long maximumWaitTime;
    // maximumAutomaticWaitTime :long;
    // standardTransferTime :long;
    // minimumTransferTime :long;
    // maximumTransferTime :long;
}
