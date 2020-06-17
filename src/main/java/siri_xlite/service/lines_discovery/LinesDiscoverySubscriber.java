package siri_xlite.service.lines_discovery;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import java.util.List;

@Slf4j
public class LinesDiscoverySubscriber extends CollectionSubscriber<LinesDiscoveryParameters> implements Constants {

    private static final String DESTINATIONS = "destinations";
    private static final String LINE_REF = "lineRef";
    private static final String LINE_NAME = "lineName";
    private static final String MONITORED = "monitored";
    private static final String DESTINATION_REF = "destinationRef";
    private static final String PLACE_NAME = "placeName";

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            writeField(writer, LINE_REF, source.getString(LINE_REF));
            writeField(writer, LINE_NAME, source.getString(LINE_NAME));
            writeField(writer, MONITORED, true);
            writeArray(writer, DESTINATIONS, source.get(DESTINATIONS, List.class), this::writeDestination);
        });
    }

    private void writeDestination(Document source) {
        writeObject(writer, source, destination -> {
            writeField(writer, DESTINATION_REF, destination.getString(DESTINATION_REF));
            writeField(writer, PLACE_NAME, destination.getString(PLACE_NAME));
        });
    }

}