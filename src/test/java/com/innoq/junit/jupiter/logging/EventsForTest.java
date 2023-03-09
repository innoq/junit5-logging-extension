package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import static org.assertj.core.api.Assertions.assertThat;

@Logging
class EventsForTest {

    static final Logger EVENTS_FOR_TEST = LoggerFactory.getLogger(EventsForTest.class);
    static final Logger LOG2 = LoggerFactory.getLogger("EventsForTest.Log2");
    static final Logger LOG3 = LoggerFactory.getLogger("EventsForTest.Log3");
    static final Logger EVENTS_FOR = LoggerFactory.getLogger(EventsFor.class);

    @Test
    void events_shouldContainAllEvents_whenNoLoggerIsSpecified(@EventsFor LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "EventsForTest message",
                "Log2 message",
                "Log3 message",
                "EventsFor message");
    }

    @Test
    void events_shouldContainOnlyMatchingEvents_whenLoggerIsSpecifiedViaValue(@EventsFor(EventsFor.class) LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsFor message");
    }

    @Test
    void events_shouldContainOnlyMatchingEvents_whenLoggerIsSpecifiedViaLogger(@EventsFor(logger = EventsForTest.class) LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsForTest message");
    }

    @Test
    void events_shouldContainOnlyValueMatchingEvents_whenLoggerIsSpecifiedViaValueAndLogger(@EventsFor(value = EventsFor.class, logger = EventsForTest.class) LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsFor message");
    }

    @Test
    void events_shouldContainOnlyMatchingEvents_whenLoggerIsSpecifiedViaName(@EventsFor(name = "EventsForTest.Log3") LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("Log3 message");
    }

    @Test
    void events_shouldContainOnlyValueMatchingEvents_whenLoggerIsSpecifiedViaValueAndName(@EventsFor(value = EventsFor.class, name = "EventsForTest.Log3") LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsFor message");
    }

    @Test
    void events_shouldContainOnlyLoggerMatchingEvents_whenLoggerIsSpecifiedViaLoggerAndName(@EventsFor(logger = EventsForTest.class, name = "EventsForTest.Log3") LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsForTest message");
    }

    @Test
    void events_shouldContainOnlyValueMatchingEvents_whenLoggerIsSpecifiedViaValueLoggerAndName(@EventsFor(value = EventsFor.class, logger = EventsForTest.class, name = "EventsForTest.Log3") LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("EventsFor message");
    }

    @Test
    void events_shouldContainEventsForAllSubLoggers_whenMultipleLoggersExistUnderneathName(@EventsFor(name = "EventsForTest") LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log2 message",
                "Log3 message");
    }

    @Test
    void events_shouldContainOnlyMatchingEvents_whenAnnotationIsRepeated(@EventsFor(EventsFor.class) @EventsFor(name = "EventsForTest.Log2") LoggingEvents events) {
        EVENTS_FOR_TEST.info("EventsForTest message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        EVENTS_FOR.info("EventsFor message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log2 message",
                "EventsFor message");
    }

    @Test
    void events_shouldContainOnlyEventsWithInfoWarnAndErrorLevel_whenLoggerIsSpecifiedWithout(@EventsFor LoggingEvents events) {
        EVENTS_FOR.trace("EventsFor trace message");
        EVENTS_FOR.debug("EventsFor debug message");
        EVENTS_FOR.info("EventsFor info message");
        EVENTS_FOR.warn("EventsFor warn message");
        EVENTS_FOR.error("EventsFor error message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "EventsFor info message",
                "EventsFor warn message",
                "EventsFor error message");
    }

    @Test
    void events_shouldContainOnlyMatchingEvents_whenLoggerIsSpecifiedWithLevel(@EventsFor(level = Level.DEBUG) LoggingEvents events) {
        EVENTS_FOR.trace("EventsFor trace message");
        EVENTS_FOR.debug("EventsFor debug message");
        EVENTS_FOR.info("EventsFor info message");
        EVENTS_FOR.warn("EventsFor warn message");
        EVENTS_FOR.error("EventsFor error message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "EventsFor debug message",
                "EventsFor info message",
                "EventsFor warn message",
                "EventsFor error message");
    }

    @Test
    void events_shouldContainOnlyMatchingEvents_whenMultipleLoggersWithDifferentLevelsAreSpecified(@EventsFor(logger = EventsFor.class, level = Level.DEBUG) @EventsFor(logger = EventsForTest.class, level = Level.WARN) LoggingEvents events) {
        EVENTS_FOR_TEST.trace("EventsForTest trace message");
        EVENTS_FOR_TEST.debug("EventsForTest debug message");
        EVENTS_FOR_TEST.info("EventsForTest info message");
        EVENTS_FOR_TEST.warn("EventsForTest warn message");
        EVENTS_FOR_TEST.error("EventsForTest error message");
        LOG2.trace("Log2 trace message");
        LOG2.debug("Log2 debug message");
        LOG2.info("Log2 info message");
        LOG2.warn("Log2 warn message");
        LOG2.error("Log2 error message");
        EVENTS_FOR.trace("EventsFor trace message");
        EVENTS_FOR.debug("EventsFor debug message");
        EVENTS_FOR.info("EventsFor info message");
        EVENTS_FOR.warn("EventsFor warn message");
        EVENTS_FOR.error("EventsFor error message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "EventsForTest warn message",
                "EventsForTest error message",
                "EventsFor debug message",
                "EventsFor info message",
                "EventsFor warn message",
                "EventsFor error message");
    }
}
