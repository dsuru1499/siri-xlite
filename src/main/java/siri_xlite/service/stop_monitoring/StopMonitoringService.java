package siri_xlite.service.stop_monitoring;

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
import reactor.core.publisher.Flux;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.Constants;
import siri_xlite.service.common.Messages;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.StopMonitoring;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static siri_xlite.repositories.VehicleJourneyRepository.COLLECTION_NAME;
import static siri_xlite.service.common.SiriSubscriber.getEtag;

@Slf4j
@Service
public class StopMonitoringService implements StopMonitoring, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");
    @Autowired
    private EmbeddedCacheManager manager;
    @Autowired
    private StopMonitoringSubscriber subscriber;
    @Autowired
    private Configuration configuration;
    @Autowired
    private VehicleJourneyRepository repository;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public StopMonitoringSubscriber stopMonitoringSubscriber() {
        return new StopMonitoringSubscriber();
    }

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOP_MONITORING);
            Flowable.fromCallable(() -> {
                StopMonitoringParameters parameters = ParametersFactory.create(StopMonitoringParameters.class, context);
                subscriber.configure(configuration, parameters, context);
                return parameters;
            }).flatMap(parameters -> stream(parameters, context)).doOnComplete(() -> onComplete(context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void onComplete(RoutingContext context) {
        String etag = subscriber.getEtag();
        if (StringUtils.isNotEmpty(etag)) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String uri = context.request().uri();
            cache.putForExternalRead(uri, etag, LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
    }

    private Flux<VehicleJourneyDocument> stream(StopMonitoringParameters parameters, RoutingContext context) {
        Cache<String, String> cache = manager.getCache(ETAGS);
        String etag = getEtag(context);
        if (StringUtils.isNotEmpty(etag)) {
            String uri = context.request().uri();
            String cached = cache.get(uri);
            if (StringUtils.equals(cached, etag)) {
                throw new NotModifiedException();
            }
        }

        log.info(messages.getString(LOAD_FROM_BACKEND), COLLECTION_NAME, "");
        return repository.findByStopPointRef(parameters.getStopPointRef());
    }

}
