package siri_xlite.service.estimated_vehicule_journey;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.repositories.StopPointsRepository;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.EstimatedVehiculeJourney;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.SiriSubscriber;

@Slf4j
@Service
public class EstimatedVehiculeJourneyService implements EstimatedVehiculeJourney {

    @Autowired
    private Configuration configuration;

    @Autowired
    private StopPointsRepository stopPointsRepository;

    @Autowired
    private VehicleJourneyRepository vehicleJourneyRepository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY);

            SiriSubscriber<Document, EstimatedVehiculeJourneyParameters> subscriber = EstimatedVehiculeJourneySubscriber
                    .create(context);
            Flowable.fromCallable(() -> {
                EstimatedVehiculeJourneyParameters parameters = ParametersFactory
                        .create(EstimatedVehiculeJourneyParameters.class, context);
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

    private Mono<VehicleJourneyDocument> stream(EstimatedVehiculeJourneyParameters parameters) {
        Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY + "-query");
        Mono<VehicleJourneyDocument> result = vehicleJourneyRepository
                .findById(parameters.getDatedVehicleJourneyRefef());
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        return result;
    }

}
