package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@Logging
class EventsForTest {

    static final Logger LOG = LoggerFactory.getLogger(EventsForTest.class);
    static final Logger LOG2 = LoggerFactory.getLogger("EventsForTest.Log2");
    static final Logger LOG3 = LoggerFactory.getLogger("EventsForTest.Log3");

    @Test
    void events_shouldContainOnlyLogEventsForSingleSpecifiedLogger(@EventsFor("com.innoq.junit.jupiter.logging.EventsForTest") LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");

        assertThat(events.all())
                .extracting(ILoggingEvent::getMessage)
                .containsExactly("Log message");
    }

    @Test
    void events_shouldContainOnlyLogEventsForMultipleSpecifiedLogger(@EventsFor({"com.innoq.junit.jupiter.logging.EventsForTest", "EventsForTest.Log3"}) LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log message",
                "Log3 message");
    }
}
