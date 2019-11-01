package siri_xlite.repositories;

import reactor.core.publisher.Flux;
import siri_xlite.model.StopPointDocument;

public interface StopPointsCustomRepository {

    Flux<StopPointDocument> findAll();

    void clearAll();

    Flux<String> findAllById(String id);
}
