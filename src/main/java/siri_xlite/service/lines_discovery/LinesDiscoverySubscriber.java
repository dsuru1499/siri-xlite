package siri_xlite.service.lines_discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.infinispan.Cache;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static siri_xlite.repositories.LinesRepository.COLLECTION_NAME;

@Slf4j
public class LinesDiscoverySubscriber extends CollectionSubscriber<LinesDiscoveryParameters> implements Constants {

    public static final String DESTINATIONS = "destinations";
    public static final String LINE_REF = "lineRef";
    public static final String LINE_NAME = "lineName";
    public static final String MONITORED = "monitored";
    public static final String DESTINATION_REF = "destinationRef";
    public static final String PLACE_NAME = "placeName";

    @Override
    public void onComplete() {
        super.onComplete();
        String etag = getEtag();
        if (StringUtils.isNotEmpty(etag)) {
            Cache<String, String> cache = manager.getCache(COLLECTION_NAME);
            cache.putForExternalRead(ALL + etag, etag, LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
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