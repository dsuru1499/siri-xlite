package siri_xlite.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import siri_xlite.model.VehicleJourney;

import java.io.IOException;
import java.util.List;

public class JourneyInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) throws IOException {

        // set vehicleJourneyName
        String vehicleJourneyName = source.vehicleJourneyName();
        if (StringUtils.isNotEmpty(vehicleJourneyName)) {
            writer.writeStringField("VehicleJourneyName", vehicleJourneyName);
        }

        // set journeyNote
        List<String> journeyNotes = source.journeyNotes();
        if (CollectionUtils.isNotEmpty(journeyNotes)) {
            writer.writeArrayFieldStart("JourneyNote");
            for (String journeyNote : journeyNotes) {
                writer.writeString(journeyNote);
            }
            writer.writeEndArray();
        }

        // publicContact :SimpleContact;
        // operationsContact:SimpleContact;
    }

}
