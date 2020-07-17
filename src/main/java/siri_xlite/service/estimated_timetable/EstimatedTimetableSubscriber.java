package siri_xlite.service.estimated_timetable;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import siri_xlite.marshaller.json.*;
import siri_xlite.service.common.CollectionSubscriber;
import siri_xlite.service.common.Constants;

import static siri_xlite.marshaller.json.EstimatedTimetableAlterationGroupMarshaller.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.service.Verticle.*;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;

@Slf4j
public class EstimatedTimetableSubscriber extends CollectionSubscriber<EstimatedTimetableParameters>
        implements Constants {

    @Override
    protected void writeItem(Document t) {
        writeObject(writer, t, source -> {
            HttpServerRequest request = context.request();
            String url = request.scheme() + COLON + SEP + SEP + request.host() + APPLICATION + SEP
                    + ESTIMATED_VEHICLE_JOURNEY + SEP + source.getString(DATED_VEHICLE_JOURNEY_REF);
            writeField(writer, HREF, url);

            LineIdentityGroupMarshaller.getInstance().write(writer, source);
            JourneyPatternInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndNamesGroupMarshaller.getInstance().write(writer, source);
            ServiceInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndTimesGroupMarshaller.getInstance().write(writer, source);

        });
    }

}