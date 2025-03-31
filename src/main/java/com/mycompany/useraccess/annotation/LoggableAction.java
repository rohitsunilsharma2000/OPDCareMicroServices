package com.mycompany.useraccess.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableAction {
    String action();
    String targetEntity();
    String targetId(); // Optional: could be dynamically resolved with SpEL if needed
}
