package siri_xlite.service;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InitializerTest {

    @Autowired
    private Initializer initializer;

    @Disabled
    @Test
    void initialize() {
        assertTrue(initializer.initialize());
    }
}