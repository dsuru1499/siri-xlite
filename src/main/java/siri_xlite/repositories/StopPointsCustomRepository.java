package siri_xlite.repositories;

import reactor.core.publisher.Flux;

public interface StopPointsCustomRepository {

    Flux<StopPointsDocument> findAll();

    Flux<String> findAllById(String id);
}
