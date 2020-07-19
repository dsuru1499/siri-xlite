package siri_xlite.service.estimated_timetable;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedTimetableParameters extends DefaultParameters {

    public static final String LINE_REF = "line_ref";

    private String lineRef;

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
        setMaxAge(3);
        setSMaxAge(30);
        setLineRef(values.get(LINE_REF));
    }

}
