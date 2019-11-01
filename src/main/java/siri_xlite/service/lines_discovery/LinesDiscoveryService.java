package siri_xlite.service.lines_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;
import siri_xlite.repositories.LinesRepository;
import siri_xlite.service.common.LinesDiscovery;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.SiriSubscriber;

@Slf4j
@Service
public class LinesDiscoveryService implements LinesDiscovery {

    @Autowired
    private Configuration configuration;

    @Autowired
    private LinesRepository repository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(LINES_DISCOVERY);

            SiriSubscriber<Document, LinesDiscoveryParameters> subscriber = LinesDiscoverySubscriber.create(context);
            Mono.fromCallable(() -> {
                LinesDiscoveryParameters parameters = ParametersFactory.create(LinesDiscoveryParameters.class, context);
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

    private Flux<LineDocument> stream(LinesDiscoveryParameters parameters) {
        Monitor monitor = MonitorFactory.start(LINES_DISCOVERY + "-query");
        Flux<LineDocument> result = repository.findAll();
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        return result;
    }

}
