package siri_xlite.service.estimated_vehicule_journey;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.Constants;
import siri_xlite.service.common.EstimatedVehiculeJourney;
import siri_xlite.service.common.Messages;
import siri_xlite.service.common.ParametersFactory;

import java.util.ResourceBundle;

import static siri_xlite.repositories.VehicleJourneyRepository.COLLECTION_NAME;
import static siri_xlite.service.common.SiriSubscriber.getEtag;

@Slf4j
@Service
public class EstimatedVehiculeJourneyService implements EstimatedVehiculeJourney, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");
    @Autowired
    protected EmbeddedCacheManager manager;
    @Autowired
    protected EstimatedVehiculeJourneySubscriber subscriber;
    @Autowired
    private Configuration configuration;
    @Autowired
    private VehicleJourneyRepository repository;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EstimatedVehiculeJourneySubscriber estimatedVehiculeJourneySubscriber() {
        return new EstimatedVehiculeJourneySubscriber();
    }

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY);

            Flowable.fromCallable(() -> {
                EstimatedVehiculeJourneyParameters parameters = ParametersFactory
                        .create(EstimatedVehiculeJourneyParameters.class, context);
                subscriber.configure(configuration, parameters, context);
                return parameters;
            }).flatMap(parameters -> stream(parameters, context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Mono<VehicleJourneyDocument> stream(EstimatedVehiculeJourneyParameters parameters, RoutingContext context) {

        Cache<String, String> cache = manager.getCache(COLLECTION_NAME);
        String etag = getEtag(context);
        if (StringUtils.isNotEmpty(etag)) {
            if (StringUtils.isNotEmpty(cache.get(etag))) {
                throw new NotModifiedException();
            }
        }

        log.info(messages.getString(LOAD_FROM_BACKEND), COLLECTION_NAME, "");
        return repository.findById(parameters.getDatedVehicleJourneyRef());
    }

}
