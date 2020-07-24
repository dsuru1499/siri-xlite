package siri_xlite.service.lines_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;
import siri_xlite.repositories.LinesRepository;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.service.common.*;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LinesDiscoveryService implements LinesDiscovery, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");
    @Autowired
    private LinesRepository repository;
    @Autowired
    private EmbeddedCacheManager manager;
    @Autowired
    private Configuration configuration;

    @Override
    public void handle(RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(LINES_DISCOVERY);
            final LinesDiscoverySubscriber subscriber = new LinesDiscoverySubscriber();
            Mono.fromCallable(() -> {
                LinesDiscoveryParameters parameters = ParametersFactory.create(LinesDiscoveryParameters.class, context);
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

    private void onComplete(LinesDiscoverySubscriber subscriber, RoutingContext context) {
        Date lastModified = subscriber.getLastModified();
        if (lastModified != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String uri = context.request().uri();
            cache.putForExternalRead(uri, String.valueOf(lastModified.getTime()), LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
    }

    private Flux<LineDocument> stream(LinesDiscoveryParameters parameters, RoutingContext context) {
        Date when = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        if (when != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String cached = cache.get(uri);
            if (StringUtils.isNotEmpty(cached)) {
                Date lastModified = new Date(Long.parseLong(cached));
                if (lastModified.after(when)) {
                    throw new NotModifiedException();
                }
            }
        }

        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        return repository.findAll();
    }

}
