package siri_xlite.service.stop_monitoring;

import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import java.util.Date;
import java.util.List;

import static siri_xlite.common.JsonUtils.*;
import static siri_xlite.marshaller.json.EstimatedTimetableAlterationGroupMarshaller.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.marshaller.json.JourneyEndNamesGroupMarshaller.DESTINATION_REF;
import static siri_xlite.marshaller.json.JourneyPatternInfoGroupMarshaller.DIRECTION_NAME;
import static siri_xlite.marshaller.json.JourneyPatternInfoGroupMarshaller.ROUTE_REF;
import static siri_xlite.marshaller.json.LineIdentityGroupMarshaller.DIRECTION_REF;
import static siri_xlite.marshaller.json.LineIdentityGroupMarshaller.LINE_REF;
import static siri_xlite.marshaller.json.OnwardVehicleArrivalTimesGroupMarshaller.AIMED_ARRIVAL_TIME;
import static siri_xlite.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.AIMED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.ServiceInfoGroupMarshaller.OPERATOR_REF;
import static siri_xlite.marshaller.json.SiriMarshaller.CALLS;
import static siri_xlite.marshaller.json.SiriMarshaller.INDEX;
import static siri_xlite.marshaller.json.StopPointInSequenceGroupMarshaller.ORDER;
import static siri_xlite.marshaller.json.StopPointInSequenceGroupMarshaller.STOP_POINT_REF;
import static siri_xlite.service.Verticle.*;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;

@Slf4j
public class StopMonitoringSubscriber extends CollectionSubscriber<StopMonitoringParameters> implements Constants {

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            HttpServerRequest request = context.request();
            Integer index = source.getInteger(INDEX);
            List<?> calls = source.get(CALLS, List.class);
            Document call = (Document) calls.get(index);

            String url = APPLICATION + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP
                    + source.getString(DATED_VEHICLE_JOURNEY_REF) + HASH + index;
            writeField(writer, HREF, url);

            // metadata
            writeStringField(writer, LINE_REF, source);
            writeStringField(writer, ROUTE_REF, source);
            writeStringField(writer, DIRECTION_REF, source);
            writeStringField(writer, DIRECTION_NAME, source);
            writeStringField(writer, DESTINATION_REF, source);
            writeStringField(writer, OPERATOR_REF, source);
            writeStringField(writer, STOP_POINT_REF, call);
            writeIntegerField(writer, ORDER, call);
            writeLocalTimeField(writer, AIMED_DEPARTURE_TIME, call);
            writeLocalTimeField(writer, AIMED_ARRIVAL_TIME, call);
        });
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writeNotFound();
            } else {
                writer.writeEndArray();
                writeEndDocument(writer);
                writeResponse(new Date());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }
}