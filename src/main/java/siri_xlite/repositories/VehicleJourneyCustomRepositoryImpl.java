package siri_xlite.repositories;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import siri_xlite.common.Color;
import siri_xlite.model.VehicleJourneyDocument;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.Sort.Order;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static siri_xlite.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.AIMED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.SiriMarshaller.CALLS;
import static siri_xlite.marshaller.json.SiriMarshaller.INDEX;
import static siri_xlite.repositories.VehicleJourneyRepository.COLLECTION_NAME;

@Slf4j
public class VehicleJourneyCustomRepositoryImpl implements VehicleJourneyCustomRepository<String> {

    static final Comparator<? super VehicleJourneyDocument> aimedDepartureTimeComparator = Comparator.comparing(t -> {
        Integer index = t.getInteger(INDEX);
        List<Document> calls = t.get(CALLS, List.class);
        Document call = calls.get(index);
        Date result = call.get(AIMED_DEPARTURE_TIME, Date.class);
        return result;
    });
    @Autowired
    private ReactiveMongoTemplate template;

    @Autowired
    private EmbeddedCacheManager manager;

    @Autowired
    private StopPointsRepository stopPointsRepository;

    @Override
    public Mono<VehicleJourneyDocument> findById(String id) {
        Monitor monitor = MonitorFactory.start(COLLECTION_NAME);
        try {
            Query query = query(where("datedVehicleJourneyRef").is(id));
            return template.findOne(query, VehicleJourneyDocument.class, COLLECTION_NAME);
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    @Override
    public Flux<VehicleJourneyDocument> findByLineRef(String id) {
        Monitor monitor = MonitorFactory.start(COLLECTION_NAME);

        try {
            Query query = query(where("lineRef").is(id));
            query.with(Sort.by(Order.by("routeRef"), Order.by("originAimedDepartureTime")));
            return template.find(query, VehicleJourneyDocument.class, COLLECTION_NAME);
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    @Override
    public Flux<VehicleJourneyDocument> findByStopPointRef(String id) {
        Monitor monitor = MonitorFactory.start(COLLECTION_NAME);
        try {
            return stopPointsRepository.findStopPointRefs(id).collectList().flatMapMany(stopPointRefs -> {
                Query query = query(where("calls.stopPointRef").in(stopPointRefs));
                // query.with(Sort.by(Order.by("originAimedDepartureTime")));
                return template.find(query, Document.class, COLLECTION_NAME).flatMap(t -> create(t, stopPointRefs))
                        .sort(aimedDepartureTimeComparator);
            });
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private Flux<VehicleJourneyDocument> create(Document document, List<String> stopPointRefs) {

        List<VehicleJourneyDocument> list = new ArrayList<>();

        List<Document> calls = document.get("calls", List.class);
        for (int i = 0; i < calls.size(); i++) {
            Document call = calls.get(i);
            if (stopPointRefs.contains(call.getString("stopPointRef"))) {
                VehicleJourneyDocument result = new VehicleJourneyDocument(document);
                result.put(INDEX, i);
                list.add(result);
            }
        }
        return Flux.fromIterable(list);
    }

    @Override
    public void clearAll() {
        manager.getCache(COLLECTION_NAME).clear();
        template.dropCollection(COLLECTION_NAME).then(template.createCollection(COLLECTION_NAME))
                .then(template.indexOps(COLLECTION_NAME)
                        .ensureIndex(new Index().unique().on("datedVehicleJourneyRef", Sort.Direction.ASC)))
                .then(template.indexOps(COLLECTION_NAME)
                        .ensureIndex(new Index().on("calls.stopPointRef", Sort.Direction.ASC)))
                .block();
    }

    // db.collection.aggregate([
    // { $match: { "calls.stopPointRef": { $in: [ "StopPoint:10:58" ] } }},
    // { $project: {
    // "datedVehicleJourneyRef" : 1,
    // "calls" : {
    // $filter: { input: "$calls", as: "call", cond: { $in: [ "$$call.stopPointRef", [ "StopPoint:10:58" ] ] }
    // }
    // }}
    // ]).toArray();

    // @Override
    // public Flux<Document> findByStopPointRef(String id) {
    // final String[] fields = {"datedVehicleJourneyRef", "lineRef", "destinationRef", "operatorRef",
    // "originAimedDepartureTime", "calls"};
    // // ArrayOperators.In in = arrayOf("call.stopPointRef").containsValue(id);
    //
    // ComparisonOperators.Eq in = valueOf("call.stopPointRef").equalToValue(id);
    // AggregationExpression filter = filter("calls").as("call").by(in);
    // MatchOperation matchStage = match(where("calls.stopPointRef").is(id));
    // ProjectionOperation projectStage = project(fields).and(filter).as("calls");
    // Aggregation aggregation = newAggregation(matchStage, projectStage);
    //
    // return template.aggregate(aggregation, COLLECTION_NAME, Document.class).flatMap(t -> {
    // List<Document> list = new ArrayList<>();
    // List<Document> calls = t.get("calls", List.class);
    // return Flux.fromIterable(calls).map(call -> {
    // Document result = new Document(t);
    // result.put(ORDER, call.getInteger(ORDER));
    // result.put(AIMED_DEPARTURE_TIME, call.getDate(AIMED_DEPARTURE_TIME));
    // return result;
    // });
    // });
    // }
}
