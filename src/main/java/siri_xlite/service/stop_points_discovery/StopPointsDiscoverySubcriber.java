package siri_xlite.service.stop_points_discovery;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;

import java.util.List;

import static siri_xlite.common.JsonUtils.*;

@Slf4j
public class StopPointsDiscoverySubcriber extends CollectionSubscriber<StopPointsDiscoveryParameters> {

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
            writeStringField(writer, STOP_POINT_REF, source);
            writeStringField(writer, STOP_NAME, source);

            writeArray(writer, LINES, (List<?>) source.get(LINE_REFS),
                    value -> writeObject(writer, value, (lineRef) -> writeField(writer, LINE_REFS, (String) lineRef)));

            writeObjectField(writer, LOCATION, source, location -> {
                writeDoubleField(writer, LONGITUDE, location);
                writeDoubleField(writer, LATITUDE, location);
            });

        });
    }

}