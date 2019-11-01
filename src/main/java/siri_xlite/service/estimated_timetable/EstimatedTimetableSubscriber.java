package siri_xlite.service.estimated_timetable;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.SiriSubscriber;

import static siri_xlite.marshaller.json.EstimatedTimetableAlterationGroupMarshaller.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.marshaller.json.JourneyEndNamesGroupMarshaller.DESTINATION_REF;
import static siri_xlite.marshaller.json.JourneyEndTimesGroupMarshaller.ORIGIN_AIMED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.JourneyPatternInfoGroupMarshaller.ROUTE_REF;
import static siri_xlite.marshaller.json.LineIdentityGroupMarshaller.LINE_REF;
import static siri_xlite.marshaller.json.ServiceInfoGroupMarshaller.OPERATOR_REF;
import static siri_xlite.service.SiriVerticle.*;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;

@Slf4j
public class EstimatedTimetableSubscriber extends CollectionSubscriber<EstimatedTimetableParameters> {

    protected EstimatedTimetableSubscriber(RoutingContext context) {
        super(context);
    }

    public static SiriSubscriber<Document, EstimatedTimetableParameters> create(RoutingContext context) {
        return new EstimatedTimetableSubscriber(context);
    }

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
        });
    }

}