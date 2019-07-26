package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.Location;
import siri_xlite.model.VehicleJourney;
import siri_xlite.service.common.SiriStructureFactory;
import uk.org.siri.siri.OccupancyEnumeration;

import java.io.IOException;

public class JourneyProgressGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyProgressGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set monitored
        boolean monitored = source.monitored();
        writer.writeBooleanField("Monitored", monitored);

        // set monitoringError
        String monitoringError = source.monitoringError();
        if (monitoringError != null && !monitoringError.isEmpty()) {
            writer.writeStringField("MonitoringError", monitoringError);
        }

        // set inCongestion
        boolean inCongestion = source.inCongestion();
        writer.writeBooleanField("InCongestion", inCongestion);

        // set inPanic
        boolean inPanic = source.inPanic();
        writer.writeBooleanField("InPanic", inPanic);

        // predictionInaccurate :bool;
        // dataSource :string;
        // confidenceLevel :string;

        // set vehicleLocation
        Location location = source.vehicleLocation();
        if (location != null) {
            writer.writeObjectFieldStart("VehicleLocation");
            writer.writeNumberField("Longitude", location.longitude());
            writer.writeNumberField("Latitude", location.latitude());
            writer.writeEndObject();

        }

        // ? locationRecordedAtTime :long;

        // set bearing
        double bearing = source.bearing();
        writer.writeNumberField("Bearing", bearing);

        // progressRate :string;
        // ? velocity : long;
        // ? engineOn :bool;

        // set occupancy
        int occupancy = source.occupancy();
        writer.writeStringField("Occupancy", OccupancyEnumeration.values()[occupancy].name());

        // set delay
        long delay = source.delay();
        writer.writeStringField("Delay", SiriStructureFactory.createDuration(delay));

        // progressStatus :[string];
        // vehicleStatus : string;

    }
}
