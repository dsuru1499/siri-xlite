package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import siri_xlite.common.Color;

import java.util.List;

@Slf4j
public class LinesCustomRepositoryImpl implements LinesCustomRepository {

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    EmbeddedCacheManager manager;

    public Flux<LinesDocument> findAll() {
        Cache<String, List<LinesDocument>> cache = manager.getCache(LinesRepository.COLLECTION_NAME);
        List<LinesDocument> result = cache.get(LinesRepository.COLLECTION_NAME);
        if (result == null) {
            log.info(Color.RED + "load " + LinesRepository.COLLECTION_NAME + "from backend" + Color.NORMAL);
            result = template.findAll(LinesDocument.class).collectList().block();
            cache.putForExternalRead(LinesRepository.COLLECTION_NAME, result);
        }

        return Flux.fromIterable(result);
    }

}
