package siri_xlite;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "siri")
@Data
public class ApplicationConfiguration {

    private String version;

    private String participantRef;

    private Integer port;

}
