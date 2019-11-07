package siri_xlite;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import siri_xlite.service.Initializer;
import siri_xlite.service.SiriVerticle;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@EnableAutoConfiguration
// @EnableCaching
@Slf4j
public class Application {

    @Autowired
    private SiriVerticle siriVerticle;

    @Autowired
    private Initializer initializer;

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    private void initialize() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(siriVerticle);
    }

    @Bean
    public EmbeddedCacheManager createCache() {
        EmbeddedCacheManager result = null;
        try {
            result = new DefaultCacheManager("infinispan.xml");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
