package siri_xlite.service.estimated_timetable;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
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
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.*;
import siri_xlite.service.common.Constants;
import siri_xlite.service.common.EstimatedTimetable;
import siri_xlite.service.common.ParametersFactory;

import java.util.ResourceBundle;

import static siri_xlite.repositories.LinesRepository.COLLECTION_NAME;
import static siri_xlite.service.common.SiriSubscriber.getEtag;

@Slf4j
@Service
public class EstimatedTimetableService implements EstimatedTimetable, Constants {

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private Configuration configuration;

    @Autowired
    private StopPointsRepository stopPointsRepository;

    @Autowired
    private VehicleJourneyRepository repository;

    @Autowired
    protected EmbeddedCacheManager manager;

    @Autowired
    protected EstimatedTimetableSubscriber subscriber;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EstimatedTimetableSubscriber estimatedTimetableSubscriber() {
        return new EstimatedTimetableSubscriber();
    }

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_TIMETABLE);

            Flowable.fromCallable(() -> {
                EstimatedTimetableParameters parameters = ParametersFactory.create(EstimatedTimetableParameters.class,
                        context);
                subscriber.configure(configuration, parameters, context);
                return parameters;
            }).flatMap(parameters -> stream(parameters, context))
                    .doAfterTerminate(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flux<VehicleJourneyDocument> stream(EstimatedTimetableParameters parameters, RoutingContext context) {
        log.info(messages.getString(LOAD_FROM_BACKEND), COLLECTION_NAME, "");

        Cache<String, String> cache = manager.getCache(COLLECTION_NAME);
        String etag = getEtag(context);
        if (StringUtils.isNotEmpty(etag)) {
            if (StringUtils.isNotEmpty(cache.get(LINE_REF + etag))) {
                throw new NotModifiedException();
            }
        }

        return repository.findByLineRef(parameters.getLineRef());
    }

}
