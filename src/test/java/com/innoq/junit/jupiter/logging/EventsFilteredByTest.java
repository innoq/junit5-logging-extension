package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@Logging
class EventsFilteredByTest {

    static final Logger EVENTS_FILTERED_BY_TEST = LoggerFactory.getLogger(EventsFilteredByTest.class);
    static final Logger LOG2 = LoggerFactory.getLogger("EventsFilteredByTest.Log2");
    static final Logger EVENTS_FILTERED_BY = LoggerFactory.getLogger(EventsFilteredBy.class);

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenSingleFilterIsDefined(@EventsFilteredBy(@EventsFor(EventsFilteredBy.class)) LoggingEvents events) {
        EVENTS_FILTERED_BY_TEST.info("EventsFilteredByTest message");
        LOG2.info("Log2 message");
        EVENTS_FILTERED_BY.info("EventsFilteredBy message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsFilteredBy message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenMultipleFiltersAreDefined(@EventsFilteredBy({@EventsFor(EventsFilteredBy.class), @EventsFor(EventsFilteredByTest.class)}) LoggingEvents events) {
        EVENTS_FILTERED_BY_TEST.info("EventsFilteredByTest message");
        LOG2.info("Log2 message");
        EVENTS_FILTERED_BY.info("EventsFilteredBy message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "EventsFilteredByTest message",
                "EventsFilteredBy message");
    }
}
