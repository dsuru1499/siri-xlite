package siri_xlite.service.stop_points_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.SiriSubscriber;

import java.util.Collection;

@Slf4j
public class StopPointsDiscoverySubcriber extends CollectionSubscriber<StopPointsDiscoveryParameters> {

    public static final String STOP_POINT_REF = "stopPointRef";
    public static final String STOP_NAME = "stopName";
    public static final String LINE_REFS = "lineRefs";
    public static final String LOCATION = "location";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String LINES = "lines";

    public StopPointsDiscoverySubcriber(RoutingContext context) {
        super(context);
    }

    public static SiriSubscriber<Document, StopPointsDiscoveryParameters> create(RoutingContext context) {
        return new StopPointsDiscoverySubcriber(context);
    }

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            writeField(writer, STOP_POINT_REF, source.getString(STOP_POINT_REF));
            writeField(writer, STOP_NAME, source.getString(STOP_NAME));
            writeArray(writer, LINES, (Collection<String>) source.get(LINE_REFS),
                    value -> writeObject(writer, value, (lineRef) -> writeField(writer, LINE_REFS, lineRef)));
            writeObject(writer, LOCATION, (Document) source.get(LOCATION), (Document location) -> {
                writeField(writer, LONGITUDE, location.getDouble(LONGITUDE));
                writeField(writer, LATITUDE, location.getDouble(LATITUDE));
            });
        });
    }

}