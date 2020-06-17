package siri_xlite.service.stop_points_discovery;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import java.util.Collection;

@Slf4j
public class StopPointsDiscoverySubcriber extends CollectionSubscriber<StopPointsDiscoveryParameters>
        implements Constants {

    private static final String STOP_POINT_REF = "stopPointRef";
    private static final String STOP_NAME = "stopName";
    private static final String LINE_REFS = "lineRefs";
    private static final String LOCATION = "location";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String LINES = "lines";

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