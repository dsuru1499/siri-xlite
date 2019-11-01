package siri_xlite.service.common;

import io.vertx.ext.web.RoutingContext;

public interface Parameters {

    void configure(RoutingContext context) throws SiriException;

    void validate() throws SiriException;

}