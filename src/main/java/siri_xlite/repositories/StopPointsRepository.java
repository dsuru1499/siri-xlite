package siri_xlite.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import siri_xlite.model.StopPointDocument;

public interface StopPointsRepository
        extends ReactiveCrudRepository<StopPointDocument, String>, StopPointsCustomRepository<String> {

    String COLLECTION_NAME = "stoppoints";

}
