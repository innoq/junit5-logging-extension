package com.innoq.junit.jupiter.logging;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, TYPE})
@Retention(RUNTIME)
@ExtendWith(LoggingExtension.class)
@Inherited
public @interface Logging {
}
