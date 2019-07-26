package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
public final class StopPoint {
    // timingPoint :bool;
    // monitored :bool;
    private String stopPointRef;
    private String stopName;
    // stopAreaRef ;
    private List<String> lineRefs;
    // features : [Feature];
    private Location location;
    // url ;
}
