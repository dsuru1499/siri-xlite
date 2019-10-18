package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.util.Collection;

import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
public class VehicleJourneyCustomRepositoryImpl implements VehicleJourneyCustomRepository {

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    EmbeddedCacheManager manager;

    @Override
    public Flux<VehicleJourneyDocument> findByStopPointRef(Flux<String> ids) {

        Collection<String> list = ids.collectList().block();
        Query query = query(Criteria.where("calls.stopPointRef").in(list));
        query.with(Sort.by("calls.aimedDepartureTime"));
        return template.find(query, VehicleJourneyDocument.class);
    }

}
