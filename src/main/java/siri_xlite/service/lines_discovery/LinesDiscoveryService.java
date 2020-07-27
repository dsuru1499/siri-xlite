package siri_xlite.service.lines_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;
import siri_xlite.repositories.EtagsRepository;
import siri_xlite.repositories.LinesRepository;
import siri_xlite.repositories.NotModifiedException;
import siri_xlite.service.common.*;

import java.util.Date;
import java.util.ResourceBundle;

import static siri_xlite.service.common.Messages.LOAD_FROM_BACKEND;

@Slf4j
@Service
public class LinesDiscoveryService extends SiriService implements LinesDiscovery {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private LinesRepository repository;
    @Autowired
    private Configuration configuration;
    @Autowired
    private EtagsRepository cache;

    @Override
    public void handle(RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(LINES_DISCOVERY);
            log(context.request());
            final LinesDiscoverySubscriber subscriber = new LinesDiscoverySubscriber();
            Mono.fromCallable(() -> {
                LinesDiscoveryParameters parameters = ParametersFactory.create(LinesDiscoveryParameters.class, configuration, context);
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

    private void onComplete(LinesDiscoverySubscriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified());
    }

    private Flux<LineDocument> stream(LinesDiscoveryParameters parameters, RoutingContext context)
            throws NotModifiedException {
        Date lastModified = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        cache.validate(uri, lastModified);

        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        return repository.findAll();
    }

}
