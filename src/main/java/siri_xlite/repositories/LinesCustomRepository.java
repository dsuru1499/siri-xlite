package siri_xlite.repositories;

import reactor.core.publisher.Flux;

public interface LinesCustomRepository {
    Flux<LinesDocument> findAll();
}
