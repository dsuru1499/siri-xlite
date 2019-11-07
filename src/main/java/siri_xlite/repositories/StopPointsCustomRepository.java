package siri_xlite.repositories;

import reactor.core.publisher.Flux;

public interface StopPointsCustomRepository<ID> {

    Flux<String> findAllById(ID id);

    void clearAll();

}
