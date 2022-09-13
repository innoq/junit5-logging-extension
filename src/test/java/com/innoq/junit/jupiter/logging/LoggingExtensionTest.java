package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@Logging
class LoggingExtensionTest {

    static final Logger LOG = LoggerFactory.getLogger(LoggingExtensionTest.class);
    static final Logger LOG2 = LoggerFactory.getLogger("LoggingExtensionTest.Log2");

    @Test
    void events_shouldBeEmpty_whenNothingWasLogged(LoggingEvents events) {
        assertThat(events.isEmpty()).isTrue();
    }

    @Test
    void events_shouldNotBeEmpty_whenSomethingWasLogged(LoggingEvents events) {
        LOG.info("Some log message");

        assertThat(events.isEmpty()).isFalse();
    }

    @Test
    void events_shouldContainLogEventsForEveryLevel(LoggingEvents events) {
        LOG.trace("Trace message");
        LOG.debug("Debug message");
        LOG.info("Info message");
        LOG.warn("Warn message");
        LOG.error("Error message");

        assertThat(events.all())
                .extracting(ILoggingEvent::getMessage)
                .containsExactly(
                    "Trace message",
                    "Debug message",
                    "Info message",
                    "Warn message",
                    "Error message");
    }

    @Test
    void events_shouldContainLogEventsForEveryLogger(LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");

        assertThat(events.all())
                .extracting(ILoggingEvent::getMessage)
                .containsExactly(
                    "Log message",
                    "Log2 message");
    }
}
