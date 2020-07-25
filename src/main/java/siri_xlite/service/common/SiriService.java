package siri_xlite.service.common;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j;
import siri_xlite.common.Color;

@Log4j
public abstract class SiriService implements Handler<RoutingContext> {

    protected void log(HttpServerRequest request) {
        log.info(Color.GREEN + "[DSU] GET " + request.uri() + Color.NORMAL);
        MultiMap headers = request.headers();
        for (String key : headers.names()) {
            String value = String.join(",", headers.getAll(key));
            log.info(Color.GREEN + key + "=" + value + Color.NORMAL);
        }
    }


}
