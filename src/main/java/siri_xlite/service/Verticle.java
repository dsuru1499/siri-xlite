package siri_xlite.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.FaviconHandler;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static siri_xlite.service.common.EstimatedTimetable.ESTIMATED_TIMETABLE;
import static siri_xlite.service.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;
import static siri_xlite.service.common.LinesDiscovery.LINES_DISCOVERY;
import static siri_xlite.service.common.StopMonitoring.STOP_MONITORING;
import static siri_xlite.service.common.StopPointsDiscovery.STOPPOINTS_DISCOVERY;
import static siri_xlite.service.estimated_timetable.EstimatedTimetableParameters.LINE_REF;
import static siri_xlite.service.estimated_vehicule_journey.EstimatedVehiculeJourneyParameters.DATED_VEHICLE_JOURNEY_REF;
import static siri_xlite.service.stop_monitoring.StopMonitoringParameters.STOPPOINT_REF;
import static siri_xlite.service.stop_points_discovery.StopPointsDiscoveryParameters.X_TILE;
import static siri_xlite.service.stop_points_discovery.StopPointsDiscoveryParameters.Y_TILE;

@Component
@Slf4j
public class Verticle extends AbstractVerticle {

    public static final String APPLICATION = "/siri-xlite";
    public static final String SEP = "/";
    public static final String COLON = ":";
    private static final String PUBLIC = "public";
    public static final String HASH = "#";

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
        router.route()
                .handler(CorsHandler.create("*")
                        .allowedHeaders(Stream.of("Access-Control-Allow-Origin").collect(Collectors.toSet()))
                        .allowedMethods(Stream.of(HttpMethod.GET).collect(Collectors.toSet())));

        router.get(APPLICATION).handler(defaultService);
        router.get(APPLICATION + SEP + LINES_DISCOVERY).handler(linesDiscovery);
        router.route(HttpMethod.GET,
                APPLICATION + SEP + STOPPOINTS_DISCOVERY + SEP + COLON + X_TILE + SEP + COLON + Y_TILE)
                .handler(stopPointsDiscovery);
        router.route(HttpMethod.GET, APPLICATION + SEP + STOPPOINTS_DISCOVERY + SEP).handler(stopPointsDiscovery);
        router.get(APPLICATION + SEP + STOP_MONITORING + SEP + COLON + STOPPOINT_REF).handler(stopMonitoring);
        router.get(APPLICATION + SEP + ESTIMATED_TIMETABLE + SEP + COLON + LINE_REF).handler(estimatedTimetable);
        router.get(APPLICATION + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP + COLON + DATED_VEHICLE_JOURNEY_REF)
                .handler(estimatedVehiculeJourneyService);

        router.route().handler(FaviconHandler.create());
        router.route().handler(StaticHandler.create(PUBLIC));

        // SelfSignedCertificate certificate = SelfSignedCertificate.create();
        // HttpServerOptions options = new HttpServerOptions()
        // .setSsl(true)
        // .setKeyCertOptions(certificate.keyCertOptions())
        // .setTrustOptions(certificate.trustOptions());

        HttpServerOptions options = new HttpServerOptions().setUseAlpn(true).setSsl(true)
                .setKeyStoreOptions(new JksOptions().setPath("siri-xlite.jks").setPassword("siri-xlite"));

        vertx.createHttpServer(options).requestHandler(router).listen(configuration.getPort());
    }

}
