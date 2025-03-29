package com.example.supportservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "pass123", "ADMIN");

        assertThat(request.getUsername()).isEqualTo("john");
        assertThat(request.getEmail()).isEqualTo("john@example.com");
        assertThat(request.getPassword()).isEqualTo("pass123");
        assertThat(request.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void testValidationFailsForNullOrBlankFields() {
        RegisterRequest request = new RegisterRequest("", "", "", null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(4);
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("role"))).isTrue();
    }

    @Test
    void testValidationPassesForValidRequest() {
        RegisterRequest request = new RegisterRequest("alice", "alice@example.com", "secure123", "CUSTOMER");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }
}
