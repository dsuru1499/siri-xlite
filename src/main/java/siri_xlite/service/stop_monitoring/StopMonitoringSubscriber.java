package siri_xlite.service.stop_monitoring;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.marshaller.json.*;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import java.util.List;

import static siri_xlite.common.DateTimeUtils.toLocalTime;
import static siri_xlite.marshaller.json.EstimatedTimetableAlterationGroupMarshaller.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.marshaller.json.OnwardVehicleArrivalTimesGroupMarshaller.AIMED_ARRIVAL_TIME;
import static siri_xlite.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.AIMED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.SiriMarshaller.CALLS;
import static siri_xlite.marshaller.json.SiriMarshaller.INDEX;
import static siri_xlite.service.Verticle.*;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;

@Slf4j
public class StopMonitoringSubscriber extends CollectionSubscriber<StopMonitoringParameters> implements Constants {

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            HttpServerRequest request = context.request();
            Integer index = source.getInteger(INDEX);

            String url = request.scheme() + COLON + SEP + SEP + request.host() + APPLICATION + SEP
                    + ESTIMATED_VEHICLE_JOURNEY + SEP + source.getString(DATED_VEHICLE_JOURNEY_REF) + HASH + index;
            writeField(writer, HREF, url);

            // metadata
            LineIdentityGroupMarshaller.getInstance().write(writer, source);
            JourneyPatternInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndNamesGroupMarshaller.getInstance().write(writer, source);
            ServiceInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndTimesGroupMarshaller.getInstance().write(writer, source);

            List<Document> calls = source.get(CALLS, List.class);
            Document call = calls.get(index);
            StopPointInSequenceGroupMarshaller.getInstance().write(writer, call);
            CallPropertyGroupMarshaller.getInstance().write(writer, call);
            writeField(writer, AIMED_ARRIVAL_TIME, toLocalTime(call.getDate(AIMED_ARRIVAL_TIME)));
            writeField(writer, AIMED_DEPARTURE_TIME, toLocalTime(call.getDate(AIMED_DEPARTURE_TIME)));

        });
    }
}