package siri_xlite.service.common;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Data
public abstract class DefaultParameters implements Parameters {

    protected MultiMap values;

    private LocalDateTime now;

    private String messageIdentifier;

    private String maxAge = "max-age=3";

    private String sMaxAge = "s-maxage=30";

    protected DefaultParameters() {
    }

    @Override
    public void configure(RoutingContext context) throws SiriException {
        values = context.request().params();
        setNow(LocalDateTime.now());
        setMessageIdentifier(UUID.randomUUID().toString());
    }

    @Override
    public void validate() throws SiriException {
        if (now == null) {
            throw SiriException.createInvalidDataReferencesError();
        }
        if (messageIdentifier == null) {
            throw SiriException.createInvalidDataReferencesError("messageIdentifier");
        }
    }

    protected Integer intValue(String name) {
        String value = values.get(name);
        return (StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : null);
    }

    protected Long longValue(String name) {
        String value = values.get(name);
        return (StringUtils.isNotEmpty(value) ? Long.valueOf(value) : null);
    }

    protected Float floatValue(String name) {
        String value = values.get(name);
        return (StringUtils.isNotEmpty(value) ? Float.valueOf(value) : null);
    }

    protected Double doubleValue(String name) {
        String value = values.get(name);
        return (StringUtils.isNotEmpty(value) ? Double.valueOf(value) : null);
    }

}
