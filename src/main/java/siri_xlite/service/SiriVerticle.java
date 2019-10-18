package siri_xlite.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import siri_xlite.Configuration;
import siri_xlite.service.common.DefaultService;
import siri_xlite.service.lines_discovery.LinesDiscoveryService;
import siri_xlite.service.stop_monitoring.StopMonitoringService;
import siri_xlite.service.stop_points_discovery.StopPointsDiscoveryService;

import static siri_xlite.service.common.LinesDiscovery.LINES_DISCOVERY;
import static siri_xlite.service.common.StopMonitoring.STOP_MONITORING;
import static siri_xlite.service.common.StopPointsDiscovery.STOPPOINTS_DISCOVERY;
import static siri_xlite.service.stop_monitoring.StopMonitoringParameters.STOPPOINT;

@Component
public class SiriVerticle extends AbstractVerticle {

    private static final String PATH = "/siri-xlite";
    private static final String SEP = "/";
    private static final String COLON = ":";

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

    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.get(PATH).handler(defaultService);
        router.get(PATH + SEP + LINES_DISCOVERY).handler(linesDiscovery);
        router.get(PATH + SEP + STOPPOINTS_DISCOVERY).handler(stopPointsDiscovery);
        router.get(PATH + SEP + STOP_MONITORING + SEP + COLON + STOPPOINT).handler(stopMonitoring);

        router.route().handler(StaticHandler.create("public"));

        vertx.createHttpServer().requestHandler(router).listen(configuration.getPort());
    }

}
