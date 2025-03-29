package com.example.supportservice.dto;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginRequestTest {

    private final Validator validator;

    public LoginRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        LoginRequest request = new LoginRequest("john.doe", "password123");

        assertThat(request.getUsername()).isEqualTo("john.doe");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    void testValidationFailsForBlankFields() {
        LoginRequest request = new LoginRequest("", "");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password"))).isTrue();
    }

    @Test
    void testValidationPassesForValidFields() {
        LoginRequest request = new LoginRequest("admin", "secret");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
