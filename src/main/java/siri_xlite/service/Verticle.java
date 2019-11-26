package siri_xlite.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import siri_xlite.Configuration;
import siri_xlite.service.common.DefaultService;
import siri_xlite.service.estimated_timetable.EstimatedTimetableService;
import siri_xlite.service.estimated_vehicule_journey.EstimatedVehiculeJourneyService;
import siri_xlite.service.lines_discovery.LinesDiscoveryService;
import siri_xlite.service.stop_monitoring.StopMonitoringService;
import siri_xlite.service.stop_points_discovery.StopPointsDiscoveryService;

import static siri_xlite.service.common.EstimatedTimetable.ESTIMATED_TIMETABLE;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;
import static siri_xlite.service.common.LinesDiscovery.LINES_DISCOVERY;
import static siri_xlite.service.common.StopMonitoring.STOP_MONITORING;
import static siri_xlite.service.common.StopPointsDiscovery.STOPPOINTS_DISCOVERY;
import static siri_xlite.service.estimated_timetable.EstimatedTimetableParameters.LINE_REF;
import static siri_xlite.service.estimated_vehicule_journey.EstimatedVehiculeJourneyParameters.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.service.stop_monitoring.StopMonitoringParameters.STOPPOINT_REF;

@Component
@Slf4j
public class Verticle extends AbstractVerticle {

    public static final String APPLICATION = "/siri-xlite";
    public static final String SEP = "/";
    public static final String COLON = ":";
    public static final String PUBLIC = "public";

    @Autowired
    Configuration configuration;

    @Autowired
    DefaultService defaultService;

    @Autowired
    LinesDiscoveryService linesDiscovery;

    @Autowired
    StopPointsDiscoveryService stopPointsDiscovery;

    @Autowired
    StopMonitoringService stopMonitoring;

    @Autowired
    EstimatedTimetableService estimatedTimetable;

    @Autowired
    EstimatedVehiculeJourneyService estimatedVehiculeJourneyService;

    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.get(APPLICATION).handler(defaultService);
        router.get(APPLICATION + SEP + LINES_DISCOVERY).handler(linesDiscovery);
        router.get(APPLICATION + SEP + STOPPOINTS_DISCOVERY).handler(stopPointsDiscovery);
        router.get(APPLICATION + SEP + STOP_MONITORING + SEP + COLON + STOPPOINT_REF).handler(stopMonitoring);
        router.get(APPLICATION + SEP + ESTIMATED_TIMETABLE + SEP + COLON + LINE_REF).handler(estimatedTimetable);
        router.get(APPLICATION + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP + COLON + DATED_VEHICLE_JOURNEY_REF)
                .handler(estimatedVehiculeJourneyService);

        router.route().handler(StaticHandler.create(PUBLIC));

        HttpServerOptions options = new HttpServerOptions()
                .setUseAlpn(true)
                .setSsl(true)
                .setKeyStoreOptions(new JksOptions()
                        .setPath("/home/user/Projects/siri-xlite/src/main/resources/keystore.jks")
                        .setPassword("siri-xlite"))
                ;

        vertx.createHttpServer(options).requestHandler(router).listen(configuration.getPort());
    }

}
