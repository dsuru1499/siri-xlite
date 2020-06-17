package siri_xlite.repositories;

import org.springframework.data.geo.Shape;
import reactor.core.publisher.Flux;
import siri_xlite.model.StopPointDocument;

public interface StopPointsCustomRepository<ID> {

    Flux<String> findStopPointRefs(ID id);

    Flux<StopPointDocument> findAllByLocation(Shape shape);

    void clearAll();

}
