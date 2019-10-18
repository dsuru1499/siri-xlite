package siri_xlite.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PurgeServiceTest {

    @Autowired
    PurgeService service;

    @Test
    void purge() {
        assertTrue(service.purge("http://127.0.0.1/siri-xlite/lines-discovery"));
    }
}