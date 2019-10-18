package siri_xlite.common;

import io.vertx.ext.web.RoutingContext;
import siri_xlite.service.common.SiriException;

public interface Parameters {

    void configure(RoutingContext context) throws SiriException;

    void validate() throws SiriException;

}