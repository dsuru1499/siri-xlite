package siri_xlite.service.common;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultService extends SiriService {

    @Override
    public void handle(final RoutingContext context) {
        try {
            context.response().end();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
