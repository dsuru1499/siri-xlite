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

    public static final String STOPPOINT = "stopppoint";

    private String stopPoint;

    private String MonitoringRef;
    private Integer MaximumNumberOfCallsPrevious;
    private Integer MaximumNumberOfCallsOnwards;
    private StopVisitTypeEnumeration StopVisitTypes = StopVisitTypeEnumeration.ALL;

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
        setStopPoint(values.get(STOPPOINT));
    }

    @Override
    public void validate() throws SiriException {
        super.validate();
    }

}
