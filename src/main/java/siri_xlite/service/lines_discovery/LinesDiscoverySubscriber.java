package siri_xlite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.SiriSubscriber;

import java.util.List;

@Slf4j
public class LinesDiscoverySubscriber extends CollectionSubscriber<LinesDiscoveryParameters> {

    public static final String DESTINATIONS = "destinations";
    public static final String LINE_REF = "lineRef";
    public static final String LINE_NAME = "lineName";
    public static final String MONITORED = "monitored";
    public static final String DESTINATION_REF = "destinationRef";
    public static final String PLACE_NAME = "placeName";

    protected LinesDiscoverySubscriber(RoutingContext context) {
        super(context);
    }

    public static SiriSubscriber<Document, LinesDiscoveryParameters> create(RoutingContext context) {
        return new LinesDiscoverySubscriber(context);
    }

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