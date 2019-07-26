package siri_xlite.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import siri_xlite.ApplicationConfiguration;
import siri_xlite.service.lines_discovery.LinesDiscoveryService;

@Component
public class SiriVerticle extends AbstractVerticle {

    @Autowired
    ApplicationConfiguration configuration;

    @Autowired
    LinesDiscoveryService linesDiscovery;

    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);
        router.get("/siri-xlite/lines-discovery").handler(linesDiscovery);
        router.route().handler(StaticHandler.create("public"));

        vertx.createHttpServer().requestHandler(router).listen(configuration.getPort());
    }

}
