package siri_xlite.service.stop_monitoring;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.http.MediaType;
import siri_xlite.marshaller.json.SiriExceptionMarshaller;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;
import siri_xlite.service.common.SiriException;

import java.util.Arrays;
import java.util.List;

import static siri_xlite.common.DateTimeUtils.toLocalTime;
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

            String url = request.scheme() + COLON + SEP + SEP + request.host() + APPLICATION + SEP
                    + ESTIMATED_VEHICLE_JOURNEY + SEP + source.getString(DATED_VEHICLE_JOURNEY_REF) + HASH + index;
            writeField(writer, HREF, url);

            // metadata
            writeField(writer, LINE_REF, source.getString(LINE_REF));
            writeField(writer, ROUTE_REF, source.getString(ROUTE_REF));
            writeField(writer, DIRECTION_REF, source.getString(DIRECTION_REF));
            writeField(writer, DIRECTION_NAME, source.getString(DIRECTION_NAME));
            writeField(writer, DESTINATION_REF, source.getString(DESTINATION_REF));
            writeField(writer, OPERATOR_REF, source.getString(OPERATOR_REF));
            writeField(writer, STOP_POINT_REF, call.getString(STOP_POINT_REF));
            writeField(writer, ORDER, call.getInteger(ORDER));
            writeField(writer, AIMED_DEPARTURE_TIME, toLocalTime(call.getDate(AIMED_DEPARTURE_TIME)));
            writeField(writer, AIMED_ARRIVAL_TIME, toLocalTime(call.getDate(AIMED_ARRIVAL_TIME)));
        });
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                SiriExceptionMarshaller.getInstance().write(writer, SiriException.createInvalidDataReferencesError());
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(out.toString());
            } else {
                writer.writeEndArray();
                writeEndDocument(writer);
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL, Arrays.asList(PUBLIC, MAX_AGE + parameters.getMaxAge()))
                        .end(out.toString());
                ;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }
}