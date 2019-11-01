package siri_xlite.service.estimated_timetable;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.repositories.StopPointsRepository;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.EstimatedTimetable;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.SiriSubscriber;

@Slf4j
@Service
public class EstimatedTimetableService implements EstimatedTimetable {

    @Autowired
    private Configuration configuration;

    @Autowired
    private StopPointsRepository stopPointsRepository;

    @Autowired
    private VehicleJourneyRepository vehicleJourneyRepository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_TIMETABLE);

            SiriSubscriber<Document, EstimatedTimetableParameters> subscriber = EstimatedTimetableSubscriber
                    .create(context);
            Flowable.fromCallable(() -> {
                EstimatedTimetableParameters parameters = ParametersFactory.create(EstimatedTimetableParameters.class,
                        context);
                subscriber.configure(configuration, parameters);
                return parameters;
            }).flatMap(this::stream).doAfterTerminate(() -> {
                subscriber.close();
                log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
            }).subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flux<VehicleJourneyDocument> stream(EstimatedTimetableParameters parameters) {
        Monitor monitor = MonitorFactory.start(ESTIMATED_TIMETABLE + "-query");
        Flux<VehicleJourneyDocument> result = vehicleJourneyRepository.findByLineRef(parameters.getLineRef());
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        return result;
    }

}
