package siri_xlite.service.estimated_vehicule_journey;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.common.ItemSubscriber;
import siri_xlite.common.marshaller.json.*;

import java.util.List;

import static siri_xlite.common.JsonUtils.*;
import static siri_xlite.common.marshaller.json.SiriMarshaller.CALLS;

@Slf4j
public class EstimatedVehiculeJourneySubscriber extends ItemSubscriber<EstimatedVehiculeJourneyParameters> {

    private static final String EXTRA_CALL = "extraCall";
    private static final String CANCELLATION = "cancellation";
    private static final String ESTIMATED_CALLS = "estimatedCalls";

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            LineIdentityGroupMarshaller.getInstance().write(writer, source);
            EstimatedTimetableAlterationGroupMarshaller.getInstance().write(writer, source);
            JourneyPatternInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndNamesGroupMarshaller.getInstance().write(writer, source);
            ServiceInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndTimesGroupMarshaller.getInstance().write(writer, source);
            DisruptionGroupMarshaller.getInstance().write(writer, source);
            JourneyProgressGroupMarshaller.getInstance().write(writer, source);
            TrainOperationalInfoGroupMarshaller.getInstance().write(writer, source);

            // EstimatedCalls calls
            List<?> calls = source.get(CALLS, List.class);
            writeArray(writer, ESTIMATED_CALLS, calls, this::writeEstimatedCall);
        });
    }

    private void writeEstimatedCall(Object o) {
        if (o instanceof Document) {
            Document document = (Document) o;
            writeObject(writer, document, source -> {
                StopPointInSequenceGroupMarshaller.getInstance().write(writer, source);

                // setExtraCall
                writeBooleanField(writer, EXTRA_CALL, source);

                // cancellation
                writeBooleanField(writer, CANCELLATION, source);

                // CallRealTimeInfoGroupMarshaller.getInstance().write(writer, source);
                CallPropertyGroupMarshaller.getInstance().write(writer, source);
                // CallNoteGroupMarshaller.getInstance().write(writer, source);
                DisruptionGroupMarshaller.getInstance().write(writer, source);
                OnwardVehicleArrivalTimesGroupMarshaller.getInstance().write(writer, source);
                MonitoredStopArrivalStatusGroupMarshaller.getInstance().write(writer, source);
                OnwardVehicleDepartureTimesGroupMarshaller.getInstance().write(writer, source);
                // PassengerDepartureTimesGroupMarshaller.getInstance().write(writer, source);
                MonitoredStopDepartureStatusGroupMarshaller.getInstance().write(writer, source);
                HeadwayIntervalGroupMarshaller.getInstance().write(writer, source);
                StopProximityGroupMarshaller.getInstance().write(writer, source);
            });
        }
    }
}