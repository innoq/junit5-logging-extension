package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@Logging
class EventsFilteredByTest {

    static final Logger LOG = LoggerFactory.getLogger(EventsFilteredByTest.class);
    static final Logger LOG2 = LoggerFactory.getLogger("EventsFilteredByTest.Log2");
    static final Logger LOG3 = LoggerFactory.getLogger("EventsFilteredByTest.Log3");
    static final Logger LOG4 = LoggerFactory.getLogger(EventsFilteredBy.class);

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenSingleFilterIsDefined(@EventsFilteredBy(@EventsFor(clazz = EventsFilteredByTest.class)) LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("Log message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenMultipleFiltersAreDefined(@EventsFilteredBy({@EventsFor(clazz = EventsFilteredByTest.class), @EventsFor(clazz = EventsFilteredBy.class)}) LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log message",
                "Log4 message");
    }
}
