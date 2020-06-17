package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
final class Via {
    // viaPriority :long;
    private String placeRef;
    private String placeName;
    // placeShortName :string;
}
