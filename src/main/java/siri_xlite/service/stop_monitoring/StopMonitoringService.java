package siri_xlite.service.stop_monitoring;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import siri_xlite.Configuration;
import siri_xlite.common.*;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.VehicleJourneyRepository;

import java.util.ResourceBundle;

import static siri_xlite.common.Messages.LOAD_FROM_BACKEND;

@Slf4j
@Service
public class StopMonitoringService extends SiriService implements StopMonitoring {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");
    @Autowired
    private EmbeddedCacheManager manager;
    @Autowired
    private Configuration configuration;
    @Autowired
    private VehicleJourneyRepository repository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOP_MONITORING);
            log(context.request());
            final StopMonitoringSubscriber subscriber = new StopMonitoringSubscriber();
            Flowable.fromCallable(() -> {
                StopMonitoringParameters parameters = ParametersFactory.create(StopMonitoringParameters.class, configuration, context);
                subscriber.configure(parameters, context);
                return parameters;
            }).flatMap(parameters -> stream(parameters, context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flux<VehicleJourneyDocument> stream(StopMonitoringParameters parameters, RoutingContext context) {
        String uri = context.request().uri();
        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        return repository.findByStopPointRef(parameters.getMonitoringRef());
    }

}
