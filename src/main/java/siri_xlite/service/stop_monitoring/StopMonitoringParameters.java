package siri_xlite.service.stop_monitoring;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;
import siri_xlite.Configuration;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopMonitoringParameters extends DefaultParameters {

    public static final String MONITORING_REF = "monitoring_ref";

    private String monitoringRef;

    @Override
    public void configure(Configuration configuration, RoutingContext context) throws SiriException {
        super.configure(configuration, context);
        setMonitoringRef(values.get(MONITORING_REF));
    }

    @Delegate
    public Configuration.ServiceConfiguration getConfigurationS() {
        return configuration.getStopMonitoring();
    }

}
