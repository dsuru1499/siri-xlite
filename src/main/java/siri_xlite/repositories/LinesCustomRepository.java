package siri_xlite.repositories;

import reactor.core.publisher.Flux;
import siri_xlite.model.LineDocument;

public interface LinesCustomRepository {
    Flux<LineDocument> findAll();

    void clearAll();
}
