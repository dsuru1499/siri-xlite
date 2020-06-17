package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Shape;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import siri_xlite.model.StopPointDocument;

import java.util.Collection;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static siri_xlite.repositories.StopPointsRepository.COLLECTION_NAME;

@Slf4j
public class StopPointsCustomRepositoryImpl implements StopPointsCustomRepository<String> {

    @Autowired
    private ReactiveMongoTemplate template;

    @Autowired
    private EmbeddedCacheManager manager;

    @Override
    public Flux<String> findStopPointRefs(String id) {

        Aggregation aggregation = newAggregation(match(where("stopPointRef").is(id)),
                graphLookup(COLLECTION_NAME).startWith("$stopPointRef").connectFrom("stopPointRef").connectTo("parent")
                        .as("children"),
                project(from(field("stopPointRef"), field("children", "$children.stopPointRef"))));

        return template.aggregate(aggregation, COLLECTION_NAME, Document.class).flatMap((Document t) -> {
            Collection<String> result = (Collection<String>) t.get("children");
            result.add(t.getString("stopPointRef"));
            return Flux.fromIterable(result);
        });
    }

    @Override
    public Flux<StopPointDocument> findAllByLocation(Shape shape) {
        Query query = query(where("location").within(shape));
        return template.find(query, StopPointDocument.class, COLLECTION_NAME);
    }

    @Override
    public void clearAll() {
        manager.getCache(COLLECTION_NAME).clear();
        template.dropCollection(COLLECTION_NAME).then(template.createCollection(COLLECTION_NAME))
                .then(template.indexOps(COLLECTION_NAME)
                        .ensureIndex(new Index().unique().on("stopPointRef", Sort.Direction.ASC)))
                .then(template.indexOps(COLLECTION_NAME).ensureIndex(new Index().on("parent", Sort.Direction.ASC)))
                .then(template.indexOps(COLLECTION_NAME)
                        .ensureIndex(new GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE)))
                .block();
    }

}