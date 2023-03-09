package com.innoq.junit.jupiter.logging;

import org.slf4j.event.Level;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(PARAMETER)
@Retention(RUNTIME)
@Repeatable(EventsFilteredBy.class)
public @interface EventsFor {

    Class<?> value() default Void.class;
    Class<?> logger() default Void.class;
    String name() default "";
    Level level() default Level.INFO;
}
