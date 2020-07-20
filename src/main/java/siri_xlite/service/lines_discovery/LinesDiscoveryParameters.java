package siri_xlite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinesDiscoveryParameters extends DefaultParameters {

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
        setMaxAge(30);
        setSMaxAge(3600);
    }

}
