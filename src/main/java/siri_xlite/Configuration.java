package siri_xlite;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import siri_xlite.service.common.StopMonitoring;

import java.util.List;

@org.springframework.context.annotation.Configuration
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "siri-xlite")
@Data
public class Configuration {

    private Integer port;

    private ServiceConfiguration estimatedVehiculeJourneyService;

    private class ServiceConfiguration {
        Integer maxAge;
        Integer sMaxAge;
    }
}
