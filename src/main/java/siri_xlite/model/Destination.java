package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@Builder
public class Destination {
    String destinationRef;
    String placeName;
    // directionRef :string;
}
