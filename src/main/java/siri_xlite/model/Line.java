package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Accessors(fluent = true)
@Builder
public class Line {
    private String lineRef;
    private String lineName;
    @Singular
    private List<Destination> destinations;
}
