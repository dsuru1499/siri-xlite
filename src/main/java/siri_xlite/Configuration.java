package siri_xlite;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@org.springframework.context.annotation.Configuration
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "siri-xlite")
@Data
public class Configuration {

    private Integer port = 8443;

    private ServiceConfiguration linesDiscovery;
    private ServiceConfiguration stopPointsDiscovery;
    private ServiceConfiguration stopMonitoring;
    private ServiceConfiguration estimatedTimetable;
    private ServiceConfiguration estimatedVehicleJourney;

    @Data
    public static class ServiceConfiguration {
        private Integer maxAge = 0;
        private Integer sMaxAge = 0;
    }
}
