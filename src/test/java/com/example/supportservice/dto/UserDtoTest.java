package com.example.supportservice.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoTest {

    @Test
    void testAllArgsConstructor() {
        UserDto userDto = new UserDto("john_doe", "john@example.com");

        assertThat(userDto.getUsername()).isEqualTo("john_doe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        UserDto userDto = new UserDto();
        userDto.setUsername("jane_doe");
        userDto.setEmail("jane@example.com");

        assertThat(userDto.getUsername()).isEqualTo("jane_doe");
        assertThat(userDto.getEmail()).isEqualTo("jane@example.com");
    }
}
