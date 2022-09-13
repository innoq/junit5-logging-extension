package com.innoq.junit.jupiter.logging;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingTest {

    static final Logger LOG = LoggerFactory.getLogger(LoggingTest.class);

    @Nested
    @Logging
    class AtClass {

        @Test
        void loggingEvents_shouldBeInjectedAndWorking(LoggingEvents events) {
            LOG.info("Some log message");

            assertThat(events.isEmpty()).isFalse();
        }
    }

    @Nested
    class AtMethod {

        @Test
        void loggingEvents_shouldBeInjectedAndWorking(@Logging LoggingEvents events) {
            LOG.info("Some log message");

            assertThat(events.isEmpty()).isFalse();
        }
    }
}
