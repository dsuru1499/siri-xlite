package siri_xlite.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
class StopPoint {
    // timingPoint :bool;
    // monitored :bool;
    String stopPointRef;
    String stopName;
    // stopAreaRef ;
    List<String> lineRefs;
    // features : [Feature];
    Location location;
    // url ;
}
