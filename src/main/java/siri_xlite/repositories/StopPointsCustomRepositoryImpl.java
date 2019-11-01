package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.index.Index;
import reactor.core.publisher.Flux;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;
import siri_xlite.model.StopPointDocument;

import java.util.Collection;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static siri_xlite.repositories.StopPointsRepository.COLLECTION_NAME;

@Slf4j
public class StopPointsCustomRepositoryImpl implements StopPointsCustomRepository {

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    EmbeddedCacheManager manager;

    @Override
    public Flux<StopPointDocument> findAll() {

        Cache<String, List<StopPointDocument>> cache = manager.getCache(COLLECTION_NAME);
        List<StopPointDocument> result = cache.get(COLLECTION_NAME);
        if (result == null) {
            log.info(Color.RED + "load " + COLLECTION_NAME + "from backend" + Color.NORMAL);
            result = template.findAll(StopPointDocument.class).collectList().block();
            cache.putForExternalRead(COLLECTION_NAME, result);
        }

        return Flux.fromIterable(result);
    }

    @Override
    public void clearAll() {
        manager.getCache(LinesRepository.COLLECTION_NAME).clear();
        template.dropCollection(COLLECTION_NAME);
        template.indexOps(COLLECTION_NAME).ensureIndex(new Index().on("_parent", Sort.Direction.ASC));
    }

    @Override
    public Flux<String> findAllById(String id) {

        Aggregation aggregation = newAggregation(match(where("_id").is(id)),
                graphLookup(COLLECTION_NAME).startWith("$_id").connectFrom("_id").connectTo("_parent").as("children"),
                project(from(field("children", "$children.stopPointRef"))));

        return template.aggregate(aggregation, COLLECTION_NAME, Document.class).flatMap((Document t) -> {
            Collection<String> result = (Collection<String>) t.get("children");
            result.add(t.getString("_id"));
            return Flux.fromIterable(result);
        });
    }



}
