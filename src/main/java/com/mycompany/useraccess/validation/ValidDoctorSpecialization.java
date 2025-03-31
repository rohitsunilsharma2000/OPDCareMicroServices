package com.mycompany.useraccess.validation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoctorSpecializationValidator.class)
@Documented
public @interface ValidDoctorSpecialization {
    String message() default "Specialization must be provided for role DOCTOR";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
