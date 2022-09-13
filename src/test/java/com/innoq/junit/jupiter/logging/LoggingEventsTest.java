package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ch.qos.logback.classic.Level.INFO;
import static org.assertj.core.api.Assertions.assertThat;

class LoggingEventsTest {

    Logger logger = (Logger) LoggerFactory.getLogger(LoggingEventsTest.class);
    ListAppender<ILoggingEvent> appender = new ListAppender<>();

    LoggingEvents events = new LoggingEvents(appender);

    @BeforeEach
    void beforeEach() {
        logger.addAppender(appender);
        appender.start();
    }

    @AfterEach
    void afterEach() {
        appender.stop();
        logger.detachAppender(appender);
    }

    @Test
    void withLevel_shouldContainOnlyLogEventsForSpecifiedLevel() {
        // given
        logger.info("Info message");
        logger.error("Error message");

        // when
        final List<ILoggingEvent> infoEvents = events.withLevel(INFO);

        // then
        assertThat(infoEvents)
            .extracting(ILoggingEvent::getMessage)
            .containsExactly("Info message");
    }
}
