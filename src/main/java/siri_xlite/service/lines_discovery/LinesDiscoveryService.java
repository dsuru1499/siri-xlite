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
import siri_xlite.service.common.Constants;
import siri_xlite.repositories.LinesRepository;
import siri_xlite.repositories.Messages;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.service.common.LinesDiscovery;
import siri_xlite.service.common.ParametersFactory;

import java.util.ResourceBundle;

import static siri_xlite.repositories.LinesRepository.COLLECTION_NAME;
import static siri_xlite.service.common.SiriSubscriber.getEtag;

@Slf4j
@Service
public class LinesDiscoveryService implements LinesDiscovery, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private Configuration configuration;

    @Autowired
    protected LinesRepository repository;

    @Autowired
    protected EmbeddedCacheManager manager;

    @Autowired
    LinesDiscoverySubscriber subscriber;

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
            }).flatMapMany(parameters -> stream(parameters, context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flux<LineDocument> stream(LinesDiscoveryParameters parameters, RoutingContext context) {
        log.info(messages.getString(LOAD_FROM_BACKEND), COLLECTION_NAME, "");

        Cache<String, String> cache = manager.getCache(COLLECTION_NAME);
        String etag = getEtag(context);
        if (StringUtils.isNotEmpty(etag)) {
            if (StringUtils.isNotEmpty(cache.get(ALL + etag))) {
                throw new NotModifiedException();
            }
        }

        return repository.findAll();
    }

}
