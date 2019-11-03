package siri_xlite.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.model.LineDocument;

public interface LinesCustomRepository<ID> {

    Mono<LineDocument> findById(ID id);

    Flux<LineDocument> findAll();

    void clearAll();
}
