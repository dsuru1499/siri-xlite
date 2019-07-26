package siri_xlite.service.lines_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siri_xlite.common.Color;
import siri_xlite.repositories.LineDiscoveryCache;
import siri_xlite.repositories.LinesDiscoveryDocument;
import siri_xlite.service.common.LinesDiscovery;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.SiriException;
import siri_xlite.service.common.SiriSubscriber;

@Slf4j
@Service
public class LinesDiscoveryService implements LinesDiscovery {

    @Autowired
    private LineDiscoveryCache cache;

    protected void validate(LinesDiscoveryParameters parameters) throws SiriException {
        parameters.validate();
    }

    @Override
    public void handle(final RoutingContext context) {
        try {
            SiriSubscriber<Document, LinesDiscoveryParameters> subscriber = LinesDiscoverySubscriber.create(context);
            Monitor monitor = MonitorFactory.start("lines-discovery");
            Flowable.fromCallable(() -> {
                LinesDiscoveryParameters parameters = ParametersFactory.create(LinesDiscoveryParameters.class, context);
                subscriber.configure(parameters);
                return parameters;
            }).flatMap(this::stream).doAfterTerminate(() -> {
                subscriber.close();
                log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
            }).subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flowable<LinesDiscoveryDocument> stream(LinesDiscoveryParameters parameters) {
        Monitor monitor = MonitorFactory.start("lines-discovery-query");
        Flowable<LinesDiscoveryDocument> result = Flowable.fromIterable(cache.findAll());
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        return result;
    }

}
