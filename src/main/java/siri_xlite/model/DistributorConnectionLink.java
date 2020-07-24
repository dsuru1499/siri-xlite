package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
class DistributorConnectionLink {
    String connectionLinkCode;
    String stopPointRef;
    String stopPointName;
    Long defaultDuration;
    Long frequentTravellerDuration;
    Long occasionalTravellerDuration;
    Long impairedAccessDuration;
}
