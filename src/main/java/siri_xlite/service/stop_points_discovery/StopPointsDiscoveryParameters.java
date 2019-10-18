package siri_xlite.service.stop_points_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopPointsDiscoveryParameters extends DefaultParameters {

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
    }

    @Override
    public void validate() throws SiriException {
        super.validate();
    }

}
