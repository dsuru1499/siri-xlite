package siri_xlite.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StopPointsRepository
        extends ReactiveCrudRepository<StopPointsDocument, String>, StopPointsCustomRepository {

    String COLLECTION_NAME = "stoppoints";

}
