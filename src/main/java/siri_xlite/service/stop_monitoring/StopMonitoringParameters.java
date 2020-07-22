package siri_xlite.service.stop_monitoring;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopMonitoringParameters extends DefaultParameters {

    public static final String MONITORING_REF = "monitoring_ref";

    private String monitoringRef;

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
        setMaxAge(30);
        setSMaxAge(60);
        setMonitoringRef(values.get(MONITORING_REF));
    }

}
