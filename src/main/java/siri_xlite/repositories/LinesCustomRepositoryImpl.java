package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import reactor.core.publisher.Flux;
import siri_xlite.common.Color;
import siri_xlite.model.LineDocument;

import java.util.List;

import static siri_xlite.repositories.LinesRepository.COLLECTION_NAME;

@Slf4j
public class LinesCustomRepositoryImpl implements LinesCustomRepository {

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    EmbeddedCacheManager manager;

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
        template.dropCollection(COLLECTION_NAME);
    }

}
