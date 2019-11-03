package siri_xlite.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import siri_xlite.model.VehicleJourneyDocument;

public interface VehicleJourneyRepository
        extends ReactiveCrudRepository<VehicleJourneyDocument, String>, VehicleJourneyCustomRepository<String> {

    String COLLECTION_NAME = "vehicle_journey";

}
