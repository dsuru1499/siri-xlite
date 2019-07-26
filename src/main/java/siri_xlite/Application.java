package siri_xlite;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import siri_xlite.service.SiriVerticle;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
public class Application {

    @Autowired
    private SiriVerticle siriVerticle;

    @PostConstruct
    private void initialize() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(siriVerticle);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
