package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public final class StopAssignment {
    private String aimedQuayRef;
    private String aimedQuayName;
    private String expectedQuayRef;
    private String actualQuayRef;
}
