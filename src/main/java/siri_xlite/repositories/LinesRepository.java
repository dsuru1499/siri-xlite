package siri_xlite.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LinesRepository extends ReactiveCrudRepository<LinesDocument, String>, LinesCustomRepository {

    String COLLECTION_NAME = "lines";

}
