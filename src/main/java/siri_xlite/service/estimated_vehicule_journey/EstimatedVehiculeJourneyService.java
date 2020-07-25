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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.*;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EstimatedVehiculeJourneyService implements EstimatedVehiculeJourney, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");
    @Autowired
    protected EmbeddedCacheManager manager;
    @Autowired
    private Configuration configuration;
    @Autowired
    private VehicleJourneyRepository repository;

    @Override
    public void handle(final RoutingContext context) {

        log.info(Color.GREEN + "[DSU] GET " + context.request().uri() + Color.NORMAL);
        context.request().headers().iterator().forEachRemaining(
                (t) -> log.info(Color.GREEN + t.getKey() + "=" + t.getValue() + Color.NORMAL));

        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY);
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
        Date lastModified = subscriber.getLastModified();
        if (lastModified != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String uri = context.request().uri();
            cache.putForExternalRead(uri, String.valueOf(lastModified.getTime()), LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
    }

    private Mono<VehicleJourneyDocument> stream(EstimatedVehiculeJourneyParameters parameters, RoutingContext context) {
        Date when = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        if (when != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String cached = cache.get(uri);
            if (StringUtils.isNotEmpty(cached)) {
                Date lastModified = new Date(Long.parseLong(cached));
                if (! lastModified.after(when)) {
                    log.info(messages.getString(REVALIDATE_RESSOURCE), uri);
                    throw new NotModifiedException();
                }
            }
        }

        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        return repository.findById(parameters.getDatedVehicleJourneyRef());
    }

}
