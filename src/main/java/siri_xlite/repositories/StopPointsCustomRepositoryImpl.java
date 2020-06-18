package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import siri_xlite.model.StopPointDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

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
    public Flux<StopPointDocument> findAllByLocation(Polygon polygon) {
        List coordinates = StreamSupport.stream(polygon.spliterator(), true)
                .collect(Collector.of(ArrayList::new, (left, right) -> {
                            List<Double> value = new ArrayList<Double>(2);
                            value.add(right.getX());
                            value.add(right.getY());
                            left.add(value);
                        },
                        (left, right) -> {
                            left.addAll(right);
                            return left;
                        },
                        list -> {
                            List result = new ArrayList();
                            result.add(list);
                            return result;
                        }
                ));

        Document $geometry = new Document().append("type", "Polygon").append("coordinates", coordinates);
        Document $geoWithin = new Document("$geometry", $geometry);
        Document location = new Document("$geoWithin", $geoWithin);

        Query query = query(where("location").is(location));
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