package siri_xlite.service.stop_monitoring;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.infinispan.Cache;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import java.util.concurrent.TimeUnit;

import static siri_xlite.marshaller.json.EstimatedTimetableAlterationGroupMarshaller.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.marshaller.json.JourneyEndNamesGroupMarshaller.DESTINATION_REF;
import static siri_xlite.marshaller.json.JourneyEndTimesGroupMarshaller.ORIGIN_AIMED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.JourneyPatternInfoGroupMarshaller.ROUTE_REF;
import static siri_xlite.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.AIMED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.ServiceInfoGroupMarshaller.OPERATOR_REF;
import static siri_xlite.marshaller.json.SiriMarshaller.INDEX;
import static siri_xlite.marshaller.json.StopPointInSequenceGroupMarshaller.ORDER;
import static siri_xlite.repositories.VehicleJourneyRepository.COLLECTION_NAME;
import static siri_xlite.service.Verticle.*;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;

@Slf4j
public class StopMonitoringSubscriber extends CollectionSubscriber<StopMonitoringParameters> implements Constants {

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            HttpServerRequest request = context.request();
            String url = request.scheme() + COLON + SEP + SEP + request.host() + APPLICATION + SEP
                    + ESTIMATED_VEHICLE_JOURNEY + SEP + source.getString(DATED_VEHICLE_JOURNEY_REF);
            writeField(writer, HREF, url);
            writeField(writer, DATED_VEHICLE_JOURNEY_REF, source.getString(DATED_VEHICLE_JOURNEY_REF));
            writeField(writer, LINE_REF, source.getString(LINE_REF));
            writeField(writer, DESTINATION_REF, source.getString(DESTINATION_REF));
            writeField(writer, ROUTE_REF, source.getString(ROUTE_REF));
            writeField(writer, OPERATOR_REF, source.getString(OPERATOR_REF));
            writeField(writer, ORIGIN_AIMED_DEPARTURE_TIME, source.getDate(ORIGIN_AIMED_DEPARTURE_TIME));

            writeField(writer, AIMED_DEPARTURE_TIME, source.getDate(AIMED_DEPARTURE_TIME));
            writeField(writer, ORDER, source.getInteger(ORDER));
            writeField(writer, INDEX, source.getInteger(INDEX));
        });
    }
}