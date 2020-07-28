package siri_xlite.common;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import siri_xlite.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Data
public abstract class DefaultParameters implements Parameters {

    protected Configuration configuration;

    protected MultiMap values;

    private LocalDateTime now;

    private String messageIdentifier;

    protected DefaultParameters() {
    }

    @Override
    public void configure(Configuration configuration, RoutingContext context) throws SiriException {
        this.configuration = configuration;
        this.values = context.request().params();
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

    public abstract Integer getSMaxAge();

    public abstract Integer getMaxAge();

}
