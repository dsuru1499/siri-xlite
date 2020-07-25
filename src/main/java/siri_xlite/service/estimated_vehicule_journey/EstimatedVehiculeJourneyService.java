package siri_xlite.service.estimated_vehicule_journey;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.EtagsRepository;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.*;

import java.util.Date;
import java.util.ResourceBundle;

import static siri_xlite.service.common.Messages.LOAD_FROM_BACKEND;

@Slf4j
@Service
public class EstimatedVehiculeJourneyService extends SiriService implements EstimatedVehiculeJourney {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private Configuration configuration;
    @Autowired
    private VehicleJourneyRepository repository;
    @Autowired
    private EtagsRepository cache;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY);
            log(context.request());
            final EstimatedVehiculeJourneySubscriber subscriber = new EstimatedVehiculeJourneySubscriber();
            Flowable.fromCallable(() -> {
                EstimatedVehiculeJourneyParameters parameters = ParametersFactory
                        .create(EstimatedVehiculeJourneyParameters.class, context);
                subscriber.configure(configuration, parameters, context);
                return parameters;
            }).flatMap(parameters -> stream(parameters, context)).doOnComplete(() -> onComplete(subscriber, context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void onComplete(EstimatedVehiculeJourneySubscriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified());
    }

    private Mono<VehicleJourneyDocument> stream(EstimatedVehiculeJourneyParameters parameters, RoutingContext context)
            throws NotModifiedException {
        Date lastModified = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        cache.validate(uri, lastModified);
        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        return repository.findById(parameters.getDatedVehicleJourneyRef());
    }

}
