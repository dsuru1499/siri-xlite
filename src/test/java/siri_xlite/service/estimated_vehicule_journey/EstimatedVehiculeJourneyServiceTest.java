package siri_xlite.service.estimated_vehicule_journey;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static siri_xlite.common.HttpUtils.*;

@SpringBootTest
@Slf4j
class EstimatedVehiculeJourneyServiceTest {

    @Test
    void estimatedVehiculeJourney() throws Exception {
        HttpResponse<String> response = get(
                "https://localhost:8443/siri-xlite/estimated-vehicle-journey/107475559-1_487653");
        printHeader(response);
        prettyPrint(response);

        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
        // TODO test response values
    }

}