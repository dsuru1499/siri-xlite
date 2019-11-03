package siri_xlite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import siri_xlite.service.Initializer;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTest {

    @Autowired
    Initializer service;

    @Test
    void initialize() {
        assertTrue(service.initialize());
    }

}