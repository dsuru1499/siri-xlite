package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;
import siri_xlite.model.VehicleJourneyDocument;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static siri_xlite.repositories.LinesRepository.COLLECTION_NAME;

@Slf4j
public class LinesCustomRepositoryImpl implements LinesCustomRepository<String> {

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    EmbeddedCacheManager manager;

    @Override
    public Mono<LineDocument> findById(String id) {
        Query query = query(where("lineRef").is(id));
        return template.findOne(query, LineDocument.class, COLLECTION_NAME);
    }

    @Override
    public Flux<LineDocument> findAll() {
        Cache<String, List<LineDocument>> cache = manager.getCache(COLLECTION_NAME);
        cache.clear();
        List<LineDocument> result = cache.get(COLLECTION_NAME);
        if (result == null) {
            log.info(Color.RED + "load " + COLLECTION_NAME + "from backend" + Color.NORMAL);
            result = template.findAll(LineDocument.class).collectList().block();
            cache.putForExternalRead(COLLECTION_NAME, result);
        }

        return Flux.fromIterable(result);
    }

    @Override
    public void clearAll() {
        manager.getCache(COLLECTION_NAME).clear();
        template.dropCollection(COLLECTION_NAME)
                .then(template.createCollection(COLLECTION_NAME))
                .then(template.indexOps(COLLECTION_NAME).ensureIndex(new Index().unique().on("lineRef", Sort.Direction.ASC)))
                .block();
    }

}
