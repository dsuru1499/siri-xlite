package siri_xlite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinesDiscoveryParametersFactory extends ParametersFactory<LinesDiscoveryParameters> {

    static {
        ParametersFactory.register(LinesDiscoveryParameters.class, new LinesDiscoveryParametersFactory());
    }

    @Override
    protected LinesDiscoveryParameters create(RoutingContext context) throws Exception {
        LinesDiscoveryParameters parameters = new LinesDiscoveryParameters();
        parameters.configure(context);
        parameters.validate();
        return parameters;
    }
}
