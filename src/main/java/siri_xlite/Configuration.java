package siri_xlite;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@org.springframework.context.annotation.Configuration
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "siri")
@Data
public class Configuration {

    private Integer port;
    private String version;
    private String participantRef;
    private String producerDomain;
    private String producerName;

}
