package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
class JourneyPart {
    private String journeyPartRef;
    private String trainNumberRef;
    // operatorRef :string;
}
