package siri_xlite.service;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import siri_xlite.common.PurgeService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PurgeServiceTest {

    @Autowired
    PurgeService service;

    @Disabled
    @Test
    void purge() {
        assertTrue(service.purge("http://127.0.0.1/siri-xlite/lines-discovery"));
    }
}