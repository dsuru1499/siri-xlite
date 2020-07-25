package siri_xlite.repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siri_xlite.common.Color;
import siri_xlite.service.common.Messages;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static siri_xlite.service.common.Messages.REVALIDATE_RESSOURCE;

@Service
@Slf4j
public class EtagsRepository {

    private static final int LIFESPAN = 24 * 60 * 60;
    private static final int MAX_IDLE = 60 * 60;
    private static final String ETAGS = "etags";

    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Autowired
    private EmbeddedCacheManager manager;

    public void put(String uri, Date lastModified) {
        if (lastModified != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            cache.putForExternalRead(uri, String.valueOf(lastModified.getTime()), LIFESPAN, TimeUnit.SECONDS, MAX_IDLE, TimeUnit.SECONDS);
        }
    }

    public void validate(String uri, Date when) throws NotModifiedException {
        if (when != null) {
            Cache<String, String> cache = manager.getCache(ETAGS);
            String cached = cache.get(uri);
            if (StringUtils.isNotEmpty(cached)) {
                Date lastModified = new Date(Long.parseLong(cached));
                if (lastModified.getTime() >= when.getTime()) {
                    log.info(messages.getString(REVALIDATE_RESSOURCE), uri);
                    throw new NotModifiedException();
                }
            }
        }
    }
}
