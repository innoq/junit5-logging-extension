package com.innoq.junit.jupiter.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.platform.commons.support.AnnotationSupport.findRepeatableAnnotations;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public final class LoggingExtension implements BeforeTestExecutionCallback, ParameterResolver {

    private static final String APPENDER = "APPENDER";

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        final Store store = getStore(extensionContext);

        final ListAppender<ILoggingEvent> appender = new ListAppender<>();

        final List<EventsFor> configuredLoggers = Arrays.stream(extensionContext.getRequiredTestMethod().getParameters())
            .flatMap(parameter -> findRepeatableAnnotations(parameter, EventsFor.class).stream())
            .collect(Collectors.toList());

        if (configuredLoggers.isEmpty()) {
            storeLogger(store, CloseableLogger.from(LoggerFactory.getLogger(ROOT_LOGGER_NAME), org.slf4j.event.Level.INFO, appender));
        } else {
            configuredLoggers
                .stream()
                .map(eventsFor -> CloseableLogger.from(eventsFor, appender))
                .forEach(logger -> storeLogger(store, logger));
        }

        storeAppender(store, appender);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().isAssignableFrom(LoggingEvents.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        final Store store = getStore(extensionContext);
        final ListAppender<ILoggingEvent> appender = getAppender(store);
        return new LoggingEvents(appender);
    }

    private static Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(LoggingExtension.class, context.getRequiredTestMethod()));
    }

    private static void storeLogger(Store store, CloseableLogger logger) {
        store.put(logger.getName(), logger);
    }

    private static void storeAppender(Store store, ListAppender<ILoggingEvent> appender) {
        store.put(APPENDER, new CloseableAppender(appender));
    }

    private static ListAppender<ILoggingEvent> getAppender(Store store) {
        return store.get(APPENDER, CloseableAppender.class).appender;
    }

    private static final class CloseableAppender implements CloseableResource {

        private final ListAppender<ILoggingEvent> appender;

        private CloseableAppender(ListAppender<ILoggingEvent> appender) {
            this.appender = appender;
            this.appender.start();
        }

        @Override
        public void close() {
            this.appender.stop();
        }
    }

    private static final class CloseableLogger implements CloseableResource {

        private final Appender<ILoggingEvent> appender;
        private final Logger logger;
        private final Level previousLevel;

        private CloseableLogger(Logger logger, Level level, Appender<ILoggingEvent> appender) {
            this.appender = appender;
            this.logger = logger;

            this.previousLevel = this.logger.getLevel();

            this.logger.setLevel(level);
            this.logger.addAppender(appender);
        }

        public String getName() {
            return "LOGGER_" + this.logger.getName();
        }

        @Override
        public void close() {
            this.logger.setLevel(this.previousLevel);
            this.logger.detachAppender(this.appender);
        }

        public static CloseableLogger from(org.slf4j.Logger logger, org.slf4j.event.Level level, Appender<ILoggingEvent> appender) {
            return new CloseableLogger((Logger) logger, Level.toLevel(level.toString()), appender);
        }

        public static CloseableLogger from(EventsFor eventsFor, Appender<ILoggingEvent> appender) {
            return from(loggerFor(eventsFor), eventsFor.level(), appender);
        }

        private static org.slf4j.Logger loggerFor(EventsFor eventsFor) {
            if (!Void.class.equals(eventsFor.value())) {
                return LoggerFactory.getLogger(eventsFor.value());
            }
            if (!Void.class.equals(eventsFor.logger())) {
                return LoggerFactory.getLogger(eventsFor.logger());
            }
            if (!eventsFor.name().trim().isEmpty()) {
                return LoggerFactory.getLogger(eventsFor.name());
            }
            return LoggerFactory.getLogger(ROOT_LOGGER_NAME);
        }
    }
}
