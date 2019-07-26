package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public final class Via {
    // viaPriority :long;
    private String placeRef;
    private String placeName;
    // placeShortName :string;
}
