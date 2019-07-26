package siri_xlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public final class Location {
    private Double longitude;
    private Double latitude;
    // coordinates :string;
    // precision :long;
    // id :string;
    // srsName :string;
}
