package siri_xlite.service.lines_discovery;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.common.CollectionSubscriber;

import static siri_xlite.common.JsonUtils.*;

@Slf4j
public class LinesDiscoverySubscriber extends CollectionSubscriber<LinesDiscoveryParameters> {

    private static final String DESTINATIONS = "destinations";
    private static final String LINE_REF = "lineRef";
    private static final String LINE_NAME = "lineName";
    private static final String MONITORED = "monitored";
    private static final String DESTINATION_REF = "destinationRef";
    private static final String PLACE_NAME = "placeName";

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            writeStringField(writer, LINE_REF, source);
            writeStringField(writer, LINE_NAME, source);
            writeBooleanField(writer, MONITORED, source);
            writeArrayField(writer, DESTINATIONS, source, this::writeDestination);
        });
    }

    private void writeDestination(Document source) {
        writeObject(writer, source, destination -> {
            writeStringField(writer, DESTINATION_REF, destination);
            writeStringField(writer, PLACE_NAME, destination);
        });
    }

}