package siri_xlite.service.stop_monitoring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static siri_xlite.common.HttpUtils.*;

@SpringBootTest
@Slf4j
class StopMonitoringServiceTest {

    @Test
    void stopMonitoring() throws Exception {
        HttpResponse<String> response = get("https://localhost:8443/siri-xlite/stop-monitoring/StopArea:59:3893358");
        printHeader(response);
        prettyPrint(response);

        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
        // TODO test response values
    }
}