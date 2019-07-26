package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public class Destination {
    private String destinationRef;
    private String placeName;
    // directionRef :string;
}
