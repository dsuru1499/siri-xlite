package siri_xlite.service.lines_discovery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.common.Configuration;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinesDiscoveryParameters extends DefaultParameters {

    @Override
    public void configure(Configuration properties) throws SiriException {
        super.configure(properties);
    }

    @Override
    public void validate() throws SiriException {
        super.validate();
    }

}
