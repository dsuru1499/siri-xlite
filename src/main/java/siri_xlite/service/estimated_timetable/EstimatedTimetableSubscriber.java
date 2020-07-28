package siri_xlite.service.estimated_timetable;

import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.common.CollectionSubscriber;

import java.util.Date;

import static siri_xlite.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;
import static siri_xlite.common.JsonUtils.*;
import static siri_xlite.marshaller.json.EstimatedTimetableAlterationGroupMarshaller.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.marshaller.json.JourneyEndNamesGroupMarshaller.DESTINATION_REF;
import static siri_xlite.marshaller.json.JourneyPatternInfoGroupMarshaller.DIRECTION_NAME;
import static siri_xlite.marshaller.json.JourneyPatternInfoGroupMarshaller.ROUTE_REF;
import static siri_xlite.marshaller.json.LineIdentityGroupMarshaller.DIRECTION_REF;
import static siri_xlite.marshaller.json.LineIdentityGroupMarshaller.LINE_REF;
import static siri_xlite.marshaller.json.ServiceInfoGroupMarshaller.OPERATOR_REF;
import static siri_xlite.marshaller.json.SiriMarshaller.DESTINATION_EXPECTED_ARRIVAL_TIME;
import static siri_xlite.marshaller.json.SiriMarshaller.ORIGIN_EXPECTED_DEPARTURE_TIME;
import static siri_xlite.service.Verticle.APPLICATION;
import static siri_xlite.service.Verticle.SEP;

@Slf4j
public class EstimatedTimetableSubscriber extends CollectionSubscriber<EstimatedTimetableParameters> {

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            HttpServerRequest request = context.request();
            String url = APPLICATION + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP
                    + source.getString(DATED_VEHICLE_JOURNEY_REF);
            writeField(writer, HREF, url);

            // metadata
            writeStringField(writer, LINE_REF, source);
            writeStringField(writer, ROUTE_REF, source);
            writeStringField(writer, DIRECTION_REF, source);
            writeStringField(writer, DIRECTION_NAME, source);
            writeStringField(writer, DESTINATION_REF, source);
            writeStringField(writer, OPERATOR_REF, source);
            writeLocalTimeField(writer, ORIGIN_EXPECTED_DEPARTURE_TIME, source);
            writeLocalTimeField(writer, DESTINATION_EXPECTED_ARRIVAL_TIME, source);
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