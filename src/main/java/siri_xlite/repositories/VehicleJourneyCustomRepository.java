package siri_xlite.repositories;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public interface VehicleJourneyCustomRepository {

    Flux<VehicleJourneyDocument> findByStopPointRef(Flux<String> ids);
}
