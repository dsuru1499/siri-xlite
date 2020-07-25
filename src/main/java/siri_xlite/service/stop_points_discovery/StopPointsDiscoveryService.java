package siri_xlite.service.stop_points_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Polygon;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.common.OSMUtils;
import siri_xlite.model.StopPointDocument;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.repositories.StopPointsRepository;
import siri_xlite.service.common.*;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StopPointsDiscoveryService implements StopPointsDiscovery, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private EmbeddedCacheManager manager;
    @Autowired
    private Configuration configuration;
    @Autowired
    private StopPointsRepository repository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOPPOINTS_DISCOVERY);
            final StopPointsDiscoverySubcriber subscriber = new StopPointsDiscoverySubcriber();
            Mono.fromCallable(() -> {
                StopPointsDiscoveryParameters parameters = ParametersFactory.create(StopPointsDiscoveryParameters.class,
                        context);
                subscriber.configure(configuration, parameters, context);
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
        Date lastModified = subscriber.getLastModified();
        if (lastModified != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String uri = context.request().uri();
            cache.putForExternalRead(uri, String.valueOf(lastModified.getTime()), LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
    }

    private Flux<StopPointDocument> stream(StopPointsDiscoveryParameters parameters, RoutingContext context) {
        Date when = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        if (when != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String cached = cache.get(uri);
            if (StringUtils.isNotEmpty(cached)) {
                Date lastModified = new Date(Long.parseLong(cached));
                if (!lastModified.after(when)) {
                    throw new NotModifiedException();
                }
            }
        }

        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        if (parameters.getXtile() != null && parameters.getYtile() != null) {
            Polygon polygon = OSMUtils.location(parameters.getXtile(), parameters.getYtile());
            return repository.findAllByLocation(polygon);
        }
        return repository.findAll();
    }

}
