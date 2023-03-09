package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public final class LoggingEvents {

    private final ListAppender<ILoggingEvent> appender;

    LoggingEvents(ListAppender<ILoggingEvent> appender) {
        this.appender = appender;
    }

    public boolean isEmpty() {
        return all().isEmpty();
    }

    public List<ILoggingEvent> all() {
        return matching(event -> true);
    }

    public List<ILoggingEvent> withLevel(Level level) {
        return matching(event -> event.getLevel().equals(level));
    }

    private List<ILoggingEvent> matching(Predicate<ILoggingEvent> predicate) {
        return new ArrayList<>(appender.list).stream()
            .filter(predicate)
            .collect(toList());
    }
}
