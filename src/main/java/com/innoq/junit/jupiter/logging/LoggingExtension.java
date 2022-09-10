package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.LoggerFactory;

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
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(LoggingEvents.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final EventsFor eventsFor = parameterContext.getParameter().getAnnotation(EventsFor.class);
        if (eventsFor == null) {
            return new LoggingEvents(appender);
        }

        return new LoggingEvents(appender, event -> event.getLoggerName().equals(eventsFor.value()));
    }
}
