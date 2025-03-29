package com.example.supportservice.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseDTOTest {

    @Test
    void testBuilderAndGetters() {
        UserResponseDTO user = UserResponseDTO.builder()
                                              .id(1L)
                                              .username("admin")
                                              .email("admin@example.com")
                                              .role("ADMIN")
                                              .enabled(true)
                                              .token("sample.jwt.token")
                                              .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getEmail()).isEqualTo("admin@example.com");
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getToken()).isEqualTo("sample.jwt.token");
    }
}
