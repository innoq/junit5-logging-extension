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
    static final Logger LOG4 = LoggerFactory.getLogger(EventsFor.class);

    @Test
    void events_shouldContainAllLogEvents_whenNoLoggerIsSpecified(@EventsFor LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log message",
                "Log2 message",
                "Log3 message",
                "Log4 message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenSingleLoggerIsSpecifiedViaValue(@EventsFor("com.innoq.junit.jupiter.logging.EventsForTest") LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("Log message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenMultipleLoggersAreSpecifiedViaValue(@EventsFor({"com.innoq.junit.jupiter.logging.EventsForTest", "EventsForTest.Log3"}) LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log message",
                "Log3 message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenSingleLoggerIsSpecifiedViaClazz(@EventsFor(clazz = EventsForTest.class) LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("Log message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenMultipleLoggersAreSpecifiedViaClazz(@EventsFor(clazz = {EventsForTest.class, EventsFor.class}) LoggingEvents events) {
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

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenMultipleLoggersAreSpecifiedViaValueAndClazz(@EventsFor(clazz = EventsForTest.class, value = "EventsForTest.Log3") LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log message",
                "Log3 message");
    }

    @Test
    void events_shouldContainLogEventsForSubAllSubLoggers_whenMultipleLoggersExistUnderneathName(@EventsFor("EventsForTest") LoggingEvents events) {
        LOG.info("Log message");
        LOG2.info("Log2 message");
        LOG3.info("Log3 message");
        LOG4.info("Log4 message");

        assertThat(events.all())
            .extracting(ILoggingEvent::getMessage)
            .containsExactly(
                "Log2 message",
                "Log3 message");
    }

    @Test
    void events_shouldContainOnlyMatchingLogEvents_whenAnnotationIsRepeated(@EventsFor(clazz = EventsForTest.class) @EventsFor(clazz = EventsFor.class) LoggingEvents events) {
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
