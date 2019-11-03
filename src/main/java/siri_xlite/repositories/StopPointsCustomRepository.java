package siri_xlite.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.model.LineDocument;
import siri_xlite.model.StopPointDocument;

public interface StopPointsCustomRepository<ID> {

    Mono<StopPointDocument> findById(String id);

    Flux<StopPointDocument> findAll();

    void clearAll();

    Flux<String> findAllById(String id);
}
