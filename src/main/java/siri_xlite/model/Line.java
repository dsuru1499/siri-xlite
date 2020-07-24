package siri_xlite.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
class Line {
    String lineRef;
    String lineName;
    @Singular
    List<Destination> destinations;
}
