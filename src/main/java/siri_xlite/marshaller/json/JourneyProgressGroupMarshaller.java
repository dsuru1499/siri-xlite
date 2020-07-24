package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import uk.org.siri.siri.OccupancyEnumeration;

import static siri_xlite.common.JsonUtils.*;

public class JourneyProgressGroupMarshaller implements Marshaller<Document> {

    public static final String MONITORED = "monitored";
    public static final String MONITORING_ERROR = "monitoringError";
    public static final String IN_CONGESTION = "inCongestion";
    public static final String IN_PANIC = "inPanic";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String VEHICLE_LOCATION = "vehicleLocation";
    public static final String BEARING = "bearing";
    public static final String OCCUPANCY = "occupancy";
    public static final String DELAY = "delay";

    @Getter
    private static final Marshaller<Document> instance = new JourneyProgressGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set monitored
        writeBooleanField(writer, MONITORED, source);

        // set monitoringError
        writeStringField(writer, MONITORING_ERROR, source);

        // set inCongestion
        writeBooleanField(writer, IN_CONGESTION, source);

        // set inPanic
        writeBooleanField(writer, IN_PANIC, source);

        // predictionInaccurate :bool;
        // dataSource :string;
        // confidenceLevel :string;

        // set vehicleLocation
        Document vehicleLocation = source.get(VEHICLE_LOCATION, Document.class);
        writeObject(writer, VEHICLE_LOCATION, vehicleLocation, location -> {
            writeField(writer, LONGITUDE, location.getDouble(LONGITUDE));
            writeField(writer, LATITUDE, location.getDouble(LATITUDE));
        });

        // ? locationRecordedAtTime :long;

        // set bearing
        writeDoubleField(writer, BEARING, source);

        // progressRate :string;
        // ? velocity : long;
        // ? engineOn :bool;

        // set occupancy
        Integer occupancy = source.getInteger(OCCUPANCY);
        if (occupancy != null) {
            writeField(writer, MONITORING_ERROR, OccupancyEnumeration.values()[occupancy]);
        }

        // set delay
        writeDurationField(writer, DELAY, source);

        // progressStatus :[string];
        // vehicleStatus : string;

    }
}
