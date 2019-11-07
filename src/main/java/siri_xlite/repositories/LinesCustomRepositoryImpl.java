package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import static siri_xlite.repositories.LinesRepository.COLLECTION_NAME;

@Slf4j
public class LinesCustomRepositoryImpl implements LinesCustomRepository<String> {

    @Autowired
    private ReactiveMongoTemplate template;

    @Autowired
    private EmbeddedCacheManager manager;

    @Override
    public void clearAll() {
        manager.getCache(COLLECTION_NAME).clear();
        template.dropCollection(COLLECTION_NAME).then(template.createCollection(COLLECTION_NAME)).then(
                template.indexOps(COLLECTION_NAME).ensureIndex(new Index().unique().on("lineRef", Sort.Direction.ASC)))
                .block();
    }

}
