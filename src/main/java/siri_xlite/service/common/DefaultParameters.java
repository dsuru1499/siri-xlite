package siri_xlite.service.common;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Data
public abstract class DefaultParameters implements Parameters {

    protected MultiMap values;

    private LocalDateTime now;

    private String messageIdentifier;

    public DefaultParameters() {
    }

    @Override
    public void configure(RoutingContext context) throws SiriException {
        values = context.request().params();
        setNow(LocalDateTime.now());
        setMessageIdentifier(UUID.randomUUID().toString());
    }

    @Override
    public void validate() throws SiriException {
    }

}
