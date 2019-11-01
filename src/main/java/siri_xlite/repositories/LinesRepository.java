package siri_xlite.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import siri_xlite.model.LineDocument;

public interface LinesRepository extends ReactiveCrudRepository<LineDocument, String>, LinesCustomRepository {

    String COLLECTION_NAME = "lines";

}
