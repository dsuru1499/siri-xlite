package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import siri_xlite.model.JourneyPart;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;
import java.util.List;

public class TrainOperationalInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new TrainOperationalInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // trainBlockPart : [TrainBlockPart];
        // blockRef :string;
        // courseOfJourneyRef :string;
        // vehicleJourneyRef :string;
        // vehicleRef :string;
        // additionalVehicleJourneyRef :[string];
        // driverRef :string;
        // driverName :string;

        // set trainNumbers
        List<String> trainNumbers = source.trainNumbers();
        if (!CollectionUtils.isEmpty(trainNumbers)) {
            writer.writeArrayFieldStart("TrainNumbers");
            for (String trainNumber : trainNumbers) {
                writer.writeStartObject();
                writer.writeStringField("TrainNumberRef", trainNumber);
                writer.writeEndObject();
            }
            writer.writeEndArray();
        }

        // set journeyParts
        List<JourneyPart> journeyParts = source.journeyParts();
        if (!CollectionUtils.isEmpty(journeyParts)) {
            writer.writeArrayFieldStart("JourneyParts");
            for (JourneyPart journeyPart : journeyParts) {
                writer.writeStartObject();

                // set journeyPartRef
                String journeyPartRef = journeyPart.journeyPartRef();
                writer.writeStringField("JourneyPartRef", journeyPartRef);

                // set trainNumberRef
                String trainNumberRef = journeyPart.trainNumberRef();
                writer.writeStringField("TrainNumberRef", trainNumberRef);

                writer.writeEndObject();
            }
            writer.writeEndArray();
        }
    }
}