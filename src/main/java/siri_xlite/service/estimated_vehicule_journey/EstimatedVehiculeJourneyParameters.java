package siri_xlite.service.estimated_vehicule_journey;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_xlite.service.common.DefaultParameters;
import siri_xlite.service.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedVehiculeJourneyParameters extends DefaultParameters {

    public static final String DATED_VEHICLE_JOURNEY_REF = "dated_vehicle_journey_ref";

    private String datedVehicleJourneyRef;

    @Override
    public void configure(RoutingContext context) throws SiriException {
        super.configure(context);
        setDatedVehicleJourneyRef(values.get(DATED_VEHICLE_JOURNEY_REF));
    }

}
