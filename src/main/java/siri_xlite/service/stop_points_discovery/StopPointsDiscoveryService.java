package siri_xlite.service.stop_points_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.StopPointDocument;
import siri_xlite.repositories.StopPointsRepository;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.SiriSubscriber;
import siri_xlite.service.common.StopPointsDiscovery;

@Log4j
@Service("siri_lite.stop_points_discovery")
public class StopPointsDiscoveryService implements StopPointsDiscovery {

    @Autowired
    private Configuration configuration;

    @Autowired
    private StopPointsRepository repository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOPPOINTS_DISCOVERY);

            SiriSubscriber<Document, StopPointsDiscoveryParameters> subscriber = StopPointsDiscoverySubcriber
                    .create(context);
            Mono.fromCallable(() -> {
                StopPointsDiscoveryParameters parameters = ParametersFactory.create(StopPointsDiscoveryParameters.class,
                        context);
                subscriber.configure(configuration, parameters);
                return parameters;
            }).flatMapMany(this::stream).doAfterTerminate(() -> {
                subscriber.close();
                log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
            }).subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flux<StopPointDocument> stream(StopPointsDiscoveryParameters parameters) {
        Monitor monitor = MonitorFactory.start(STOPPOINTS_DISCOVERY + "-query");
        Flux<StopPointDocument> result = repository.findAll();
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        return result;
    }

}
