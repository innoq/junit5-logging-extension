package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.extension.*;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.platform.commons.support.AnnotationSupport.findRepeatableAnnotations;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public final class LoggingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    private Logger rootLogger;
    private ListAppender<ILoggingEvent> appender;

    private Level originalLevel;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        rootLogger = ((Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME));
        appender = new ListAppender<>();

        originalLevel = rootLogger.getLevel();

        rootLogger.addAppender(appender);
        rootLogger.setLevel(Level.TRACE);

        appender.start();
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        appender.stop();

        rootLogger.setLevel(originalLevel);
        rootLogger.detachAppender(appender);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().isAssignableFrom(LoggingEvents.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        final Predicate<ILoggingEvent> filter = findRepeatableAnnotations(parameterContext.getParameter(), EventsFor.class)
            .stream()
            .map(LoggingExtension::filterFor)
            .reduce(LoggingEvents.NO_EVENT, Predicate::or);
        return new LoggingEvents(appender, filter);
    }

    private static Predicate<ILoggingEvent> filterFor(EventsFor eventsFor) {
        final Set<String> loggersToCapture = new HashSet<>(Arrays.asList(eventsFor.value()));
        Arrays.stream(eventsFor.clazz()).map(Class::getName).forEach(loggersToCapture::add);

        if (loggersToCapture.isEmpty()) {
            return loggingEvent -> true;
        }

        return loggingEvent -> loggersToCapture.stream()
            .anyMatch(loggerName -> loggingEvent.getLoggerName().startsWith(loggerName));
    }
}
