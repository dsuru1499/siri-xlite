package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.bson.Document;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.OccupancyEnumeration;

public class JourneyProgressGroupMarshaller implements Marshaller<Document> {

    @Getter
    private static final Marshaller<Document> instance = new JourneyProgressGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Document source) {

        // set monitored
        writeField(writer, "Monitored", source.getBoolean("monitored"));

        // set monitoringError
        writeField(writer, "monitoringError", source.getString("monitoringError"));

        // set inCongestion
        writeField(writer, "InCongestion", source.getBoolean("inCongestion"));

        // set inPanic
        writeField(writer, "InPanic", source.getBoolean("inPanic"));

        // predictionInaccurate :bool;
        // dataSource :string;
        // confidenceLevel :string;

        // set vehicleLocation
        Document vehicleLocation = source.get("vehicleLocation", Document.class);
        writeObject(writer, "VehicleLocation", vehicleLocation, location -> {
            writeField(writer, "Longitude", location.getDouble("longitude"));
            writeField(writer, "Latitude", location.getDouble("latitude"));
        });

        // ? locationRecordedAtTime :long;

        // set bearing
        writeField(writer, "bearing", source.getDouble("bearing"));

        // progressRate :string;
        // ? velocity : long;
        // ? engineOn :bool;

        // set occupancy
        Integer occupancy = source.getInteger("occupancy");
        writeField(writer, "monitoringError", OccupancyEnumeration.values()[occupancy].name());

        // set delay
        long delay = source.getLong("delay");
        writeField(writer, "delay", SiriStructureFactory.createDuration(delay));

        // progressStatus :[string];
        // vehicleStatus : string;

    }
}
