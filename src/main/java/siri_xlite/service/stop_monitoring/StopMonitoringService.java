package siri_xlite.service.stop_monitoring;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.reactivex.Flowable;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import siri_xlite.Configuration;
import siri_xlite.common.Color;
import siri_xlite.repositories.StopPointsRepository;
import siri_xlite.model.VehicleJourneyDocument;
import siri_xlite.repositories.VehicleJourneyRepository;
import siri_xlite.service.common.ParametersFactory;
import siri_xlite.service.common.SiriSubscriber;
import siri_xlite.service.common.StopMonitoring;

import java.util.Comparator;

import static siri_xlite.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.AIMED_DEPARTURE_TIME;

@Slf4j
@Service
public class StopMonitoringService implements StopMonitoring {

    public static Comparator<Document> AIMED_DEPARTURE_TIME_COMPARATOR = Comparator
            .comparing(t -> t.getDate(AIMED_DEPARTURE_TIME));

    @Autowired
    private Configuration configuration;

    @Autowired
    private StopPointsRepository stopPointsRepository;

    @Autowired
    private VehicleJourneyRepository vehicleJourneyRepository;

    @Override
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOP_MONITORING);

            SiriSubscriber<Document, StopMonitoringParameters> subscriber = StopMonitoringSubscriber.create(context);
            Flowable.fromCallable(() -> {
                StopMonitoringParameters parameters = ParametersFactory.create(StopMonitoringParameters.class, context);
                subscriber.configure(configuration, parameters);
                return parameters;
            }).flatMap(this::stream).doAfterTerminate(() -> {
                subscriber.close();
                log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
            }).subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flux<VehicleJourneyDocument> stream(StopMonitoringParameters parameters) {
        Monitor monitor = MonitorFactory.start(STOP_MONITORING + "-query");
        Flux<VehicleJourneyDocument> result = stopPointsRepository.findAllById(parameters.getStopPointRef())
                .flatMap(vehicleJourneyRepository::findByStopPointRef).sort(AIMED_DEPARTURE_TIME_COMPARATOR);
        log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        return result;
    }

}
