package siri_xlite.service.stop_points_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.SiriSubscriber;

import java.util.Collection;

@Slf4j
public class StopPointsDiscoverySubcriber extends CollectionSubscriber<StopPointsDiscoveryParameters> {

    public StopPointsDiscoverySubcriber(RoutingContext context) {
        super(context);
    }

    public static SiriSubscriber<Document, StopPointsDiscoveryParameters> create(RoutingContext context) {
        return new StopPointsDiscoverySubcriber(context);
    }

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            writeField(writer, "StopPointRef", source.getString("stopPointRef"));
            writeField(writer, "stopName", source.getString("stopName"));
            writeArray(writer, "Lines", (Collection<String>) source.get("lineRefs"),
                    value -> writeObject(writer, value, (lineRef) -> writeField(writer, "LineRef", lineRef)));
            writeObject(writer, "Location", (Document) source.get("location"), (Document location) -> {
                writeField(writer, "Longitude", location.getDouble("longitude"));
                writeField(writer, "Latitude", location.getDouble("latitude"));
            });
        });
    }

}