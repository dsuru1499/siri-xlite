package siri_xlite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.SiriSubscriber;

import java.util.List;

@Slf4j
public class LinesDiscoverySubscriber extends CollectionSubscriber<LinesDiscoveryParameters> {

    protected LinesDiscoverySubscriber(RoutingContext context) {
        super(context);
    }

    public static SiriSubscriber<Document, LinesDiscoveryParameters> create(RoutingContext context) {
        return new LinesDiscoverySubscriber(context);
    }

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            writeField(writer, "LineRef", source.getString("lineRef"));
            writeField(writer, "LineName", source.getString("lineName"));
            writeField(writer, "Monitored", true);
            writeObject(writer, "Destinations", source.get("destinations", List.class),
                    (List<Document> destinations) -> writeArray(writer, "Destination", destinations,
                            this::writeDestination));
        });
    }

    private void writeDestination(Document source) {
        writeObject(writer, source, destination -> {
            writeField(writer, "DestinationRef", destination.getString("destinationRef"));
            writeField(writer, "PlaceName", destination.getString("placeName"));
        });
    }

}