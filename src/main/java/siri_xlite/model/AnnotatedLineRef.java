package siri_xlite.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Accessors(fluent = true)
@Document("lines")
class AnnotatedLineRef {
    private String lineRef;
    private String lineName;
    private List<Destination> destinations;
}
