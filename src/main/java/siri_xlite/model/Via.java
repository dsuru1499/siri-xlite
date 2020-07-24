package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
class Via {
    // viaPriority :long;
    String placeRef;
    String placeName;
    // placeShortName :string;
}
