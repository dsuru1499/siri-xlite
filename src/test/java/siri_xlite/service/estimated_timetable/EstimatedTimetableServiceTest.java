package siri_xlite.service.estimated_timetable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static siri_xlite.common.HttpUtils.*;

@SpringBootTest
@Slf4j
class EstimatedTimetableServiceTest {

    @Test
    void estimatedTimetable() throws Exception {
        HttpResponse<String> response = get("https://localhost:8443/siri-xlite/estimated-timetable/067167006:G");
        printHeader(response);
        prettyPrint(response);

        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
        // TODO test response values
    }

}