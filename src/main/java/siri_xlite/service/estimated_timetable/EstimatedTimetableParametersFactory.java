package siri_xlite.service.estimated_timetable;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedTimetableParametersFactory extends ParametersFactory<EstimatedTimetableParameters> {

    static {
        ParametersFactory.register(EstimatedTimetableParameters.class, new EstimatedTimetableParametersFactory());
    }

    @Override
    protected EstimatedTimetableParameters create(RoutingContext context) throws Exception {
        EstimatedTimetableParameters parameters = new EstimatedTimetableParameters();
        parameters.configure(context);
        parameters.validate();
        return parameters;
    }
}
