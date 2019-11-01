package siri_xlite.repositories;

import reactor.core.publisher.Flux;
import siri_xlite.model.VehicleJourneyDocument;

public interface VehicleJourneyCustomRepository {

    Flux<VehicleJourneyDocument> findByStopPointRef(String id);

    Flux<VehicleJourneyDocument> findByLineRef(String id);

    void clearAll();
}
