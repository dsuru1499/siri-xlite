package siri_xlite.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.model.VehicleJourneyDocument;

public interface VehicleJourneyCustomRepository<ID> {

    Mono<VehicleJourneyDocument> findById(ID id);

    Flux<VehicleJourneyDocument> findByLineRef(ID id);

    Flux<VehicleJourneyDocument> findByStopPointRef(ID id);

    void clearAll();
}
