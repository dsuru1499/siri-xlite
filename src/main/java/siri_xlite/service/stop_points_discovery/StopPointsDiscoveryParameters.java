package siri_xlite.service.stop_points_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;
import siri_xlite.Configuration;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopPointsDiscoveryParameters extends DefaultParameters {

    public static final String X_TILE = "x";
    public static final String Y_TILE = "y";

    private Integer xtile;
    private Integer ytile;

    @Override
    public void configure(Configuration configuration, RoutingContext context) throws SiriException {
        super.configure(configuration, context);
        setXtile(intValue(X_TILE));
        setYtile(intValue((Y_TILE)));
    }

    @Delegate
    public Configuration.ServiceConfiguration getConfigurationS() {
        return configuration.getStopPointsDiscovery();
    }

}
