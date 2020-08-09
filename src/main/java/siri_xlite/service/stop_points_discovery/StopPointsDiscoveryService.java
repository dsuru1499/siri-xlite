package siri_xlite.service.stop_points_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Polygon;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.*;
import siri_xlite.model.StopPointDocument;
import siri_xlite.repositories.EtagsRepository;
import siri_xlite.repositories.StopPointsRepository;

import java.util.Date;
import java.util.ResourceBundle;

import static siri_xlite.common.Messages.LOAD_FROM_BACKEND;
import static siri_xlite.repositories.EtagsRepository.LIFESPAN;

@Slf4j
@Service
public class StopPointsDiscoveryService extends SiriService implements StopPointsDiscovery {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private Configuration configuration;
    @Autowired
    private StopPointsRepository repository;
    @Autowired
    private EtagsRepository cache;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOPPOINTS_DISCOVERY);
            log(context.request());
            final StopPointsDiscoverySubcriber subscriber = new StopPointsDiscoverySubcriber();
            Mono.fromCallable(() -> {
                StopPointsDiscoveryParameters parameters = ParametersFactory.create(StopPointsDiscoveryParameters.class,
                        configuration, context);
                subscriber.configure(parameters, context);
                return parameters;
            }).flatMapMany(parameters -> stream(parameters, context))
                    .doOnComplete(() -> onComplete(subscriber, context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void onComplete(StopPointsDiscoverySubcriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified(), LIFESPAN);
    }

    private Flux<StopPointDocument> stream(StopPointsDiscoveryParameters parameters, RoutingContext context)
            throws NotModifiedException {
        Date lastModified = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        cache.validate(uri, lastModified);

        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        if (parameters.getXtile() != null && parameters.getYtile() != null) {
            Polygon polygon = OSMUtils.location(parameters.getXtile(), parameters.getYtile());
            return repository.findAllByLocation(polygon);
        }
        return repository.findAll();
    }

}
