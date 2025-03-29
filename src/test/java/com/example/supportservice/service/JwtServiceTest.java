package com.example.supportservice.service;

import com.example.supportservice.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtUtil jwtUtil;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtUtil = Mockito.mock(JwtUtil.class);
        jwtService = new JwtService(jwtUtil);
    }

    @Test
    void testValidateToken() {
        String token = "valid-token";
        String username = "john@example.com";

        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        boolean result = jwtService.validateToken(token, username);

        assertThat(result).isTrue();
        verify(jwtUtil, times(1)).validateToken(token, username);
    }

    @Test
    void testExtractUsername() {
        String token = "mock-token";
        when(jwtUtil.extractUsername(token)).thenReturn("john@example.com");

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("john@example.com");
        verify(jwtUtil, times(1)).extractUsername(token);
    }

    @Test
    void testIsTokenValid() {
        String token = "mock-token";
        String username = "john@example.com";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.isTokenExpired(token)).thenReturn(false);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isTrue();
        verify(jwtUtil).extractUsername(token);
        verify(jwtUtil).isTokenExpired(token);
    }
}
