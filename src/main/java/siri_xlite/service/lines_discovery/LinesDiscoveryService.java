package siri_xlite.service.lines_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
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
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;
import siri_xlite.repositories.LinesRepository;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.service.common.Constants;
import siri_xlite.service.common.LinesDiscovery;
import siri_xlite.service.common.Messages;
import siri_xlite.service.common.ParametersFactory;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static siri_xlite.service.common.SiriSubscriber.getEtag;

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
    private LinesDiscoverySubscriber subscriber;
    @Autowired
    private Configuration configuration;


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LinesDiscoverySubscriber linesDiscoverySubscriber() {
        return new LinesDiscoverySubscriber();
    }

    @Override
    public void handle(RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(LINES_DISCOVERY);
            Mono.fromCallable(() -> {
                LinesDiscoveryParameters parameters = ParametersFactory.create(LinesDiscoveryParameters.class, context);
                subscriber.configure(configuration, parameters, context);
                return parameters;
            }).flatMapMany(parameters -> stream(parameters, context)).doOnComplete(() -> onComplete(context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void onComplete(RoutingContext context) {
        String eTag = subscriber.getEtag();
        if (StringUtils.isNotEmpty(eTag)) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String uri = context.request().uri();
            cache.putForExternalRead(uri, eTag, LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
    }

    private Flux<LineDocument> stream(LinesDiscoveryParameters parameters, RoutingContext context) {
        Cache<String, String> cache = manager.getCache(ETAGS);
        String eTag = getEtag(context);
        if (StringUtils.isNotEmpty(eTag)) {
            String uri = context.request().uri();
            String cached = cache.get(uri);
            if (StringUtils.equals(cached, eTag)) {
                throw new NotModifiedException();
            }
        }

        log.info(messages.getString(LOAD_FROM_BACKEND), ETAGS, "");
        return repository.findAll();
    }

}
