package siri_xlite.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface VehicleJourneyRepository
        extends ReactiveCrudRepository<VehicleJourneyDocument, String>, VehicleJourneyCustomRepository {

    String COLLECTION_NAME = "vehicle-journey";

}
