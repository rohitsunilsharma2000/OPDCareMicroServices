package com.example.supportservice.dto;

import com.example.supportservice.enums.Priority;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTicketRequest() {
        TicketRequest request = TicketRequest.builder()
                                             .subject("Bug in login")
                                             .description("Login fails with valid credentials")
                                             .priority(Priority.HIGH)
                                             .build();

        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testMissingFieldsShouldFailValidation() {
        TicketRequest request = new TicketRequest();
        Set<ConstraintViolation<TicketRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(3);
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("subject"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("priority"))).isTrue();
    }
}
