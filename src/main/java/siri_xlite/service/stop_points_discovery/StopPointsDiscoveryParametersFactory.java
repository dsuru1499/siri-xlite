package siri_xlite.service.stop_points_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopPointsDiscoveryParametersFactory extends ParametersFactory<StopPointsDiscoveryParameters> {

    static {
        ParametersFactory.register(StopPointsDiscoveryParameters.class, new StopPointsDiscoveryParametersFactory());
    }

    @Override
    protected StopPointsDiscoveryParameters create(RoutingContext context) throws Exception {
        StopPointsDiscoveryParameters parameters = new StopPointsDiscoveryParameters();
        parameters.configure(context);
        parameters.validate();
        return parameters;
    }
}
