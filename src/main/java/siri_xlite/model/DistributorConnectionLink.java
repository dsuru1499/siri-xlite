package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public final class DistributorConnectionLink {
    private String connectionLinkCode;
    private String stopPointRef;
    private String stopPointName;
    private Long defaultDuration;
    private Long frequentTravellerDuration;
    private Long occasionalTravellerDuration;
    private Long impairedAccessDuration;
}
