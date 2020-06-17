package siri_xlite.service.stop_monitoring;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;
import uk.org.siri.siri.StopVisitTypeEnumeration;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopMonitoringParameters extends DefaultParameters {

    public static final String STOPPOINT_REF = "stoppoint";

    private String stopPointRef;

    private String MonitoringRef;
    private Integer MaximumNumberOfCallsPrevious;
    private Integer MaximumNumberOfCallsOnwards;
    private StopVisitTypeEnumeration StopVisitTypes = StopVisitTypeEnumeration.ALL;

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
        setStopPointRef(values.get(STOPPOINT_REF));
    }

}
