package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;

public class TimetableRealtimeInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new TimetableRealtimeInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {
        // set monitored
        boolean monitored = source.monitored();
        writer.writeBooleanField("Monitored", monitored);

        // set headwayService
        boolean headwayService = source.headwayService();
        writer.writeBooleanField("HeadwayService", headwayService);

    }

}