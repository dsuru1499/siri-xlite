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
import siri_xlite.common.DateTimeUtils;
import siri_xlite.model.VehicleJourneyDocument;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static siri_xlite.marshaller.json.OnwardVehicleArrivalTimesGroupMarshaller.EXPECTED_ARRIVAL_TIME;
import static siri_xlite.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.EXPECTED_DEPARTURE_TIME;
import static siri_xlite.marshaller.json.SiriMarshaller.*;
import static siri_xlite.repositories.VehicleJourneyRepository.COLLECTION_NAME;

@Slf4j
public class VehicleJourneyCustomRepositoryImpl implements VehicleJourneyCustomRepository<String> {

    static final Comparator<? super VehicleJourneyDocument> expectedDepartureTimeComparator = Comparator.comparing(t -> {
        Integer index = t.getInteger(INDEX);
        List<?> calls = t.get(CALLS, List.class);
        Document call = (Document) calls.get(index);
        return call.get(EXPECTED_DEPARTURE_TIME, Date.class);
    });

    static final BiPredicate<? super VehicleJourneyDocument, Date> expectedDepartureTimePredicate = (t, now) -> {
        Integer index = t.getInteger(INDEX);
        List<?> calls = t.get(CALLS, List.class);
        Document call = (Document) calls.get(index);
        Date result = call.get(EXPECTED_DEPARTURE_TIME, Date.class);
        return result.after(now);
    };

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
        Date now = DateTimeUtils.toDate(LocalTime.now());

        try {
            Query query = query(where("lineRef").is(id));
            return template.find(query, Document.class, COLLECTION_NAME)
                    .map(this::create)
                    .filter(t -> !t.getDate(DESTINATION_EXPECTED_ARRIVAL_TIME).before(now))
                    .sort(Comparator.comparing(t -> t.getDate(ORIGIN_EXPECTED_DEPARTURE_TIME)));

        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private VehicleJourneyDocument create(Document document) {
        List<?> calls = document.get("calls", List.class);
        Document origin = (Document) calls.get(0);
        Document destination = (Document) calls.get(calls.size() - 1);
        VehicleJourneyDocument result = new VehicleJourneyDocument(document);
        result.put(ORIGIN_EXPECTED_DEPARTURE_TIME, origin.getDate(EXPECTED_DEPARTURE_TIME));
        result.put(DESTINATION_EXPECTED_ARRIVAL_TIME, destination.getDate(EXPECTED_ARRIVAL_TIME));
        return result;
    }

    @Override
    public Flux<VehicleJourneyDocument> findByStopPointRef(String id) {
        Monitor monitor = MonitorFactory.start(COLLECTION_NAME);
        Date now = DateTimeUtils.toDate(LocalTime.now());

        try {
            return stopPointsRepository.findStopPointRefs(id).collectList().flatMapMany(stopPointRefs -> {
                Query query = query(where("calls.stopPointRef").in(stopPointRefs));
                return template.find(query, Document.class, COLLECTION_NAME)
                        .flatMap(t -> create(t, stopPointRefs))
                        .filter(t -> expectedDepartureTimePredicate.test(t, now))
                        .sort(expectedDepartureTimeComparator);
            });
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private Flux<VehicleJourneyDocument> create(Document document, List<String> stopPointRefs) {

        List<VehicleJourneyDocument> list = new ArrayList<>();

        List<?> calls = document.get("calls", List.class);
        for (int i = 0; i < calls.size(); i++) {
            Document call = (Document) calls.get(i);
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

}
