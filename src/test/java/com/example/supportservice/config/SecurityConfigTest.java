package com.example.supportservice.config;


import com.example.supportservice.filter.JwtAuthenticationFilter;
import com.example.supportservice.service.CustomUserDetailsService;
import com.example.supportservice.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Test
    void testSecurityBeansLoaded() {
        assertThat(securityFilterChain).isNotNull();
        assertThat(authenticationManager).isNotNull();
        assertThat(authenticationProvider).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(corsConfigurationSource).isNotNull();
        assertThat(jwtAuthenticationFilter).isNotNull();
        assertThat(jwtService).isNotNull();
        assertThat(userDetailsService).isNotNull();
    }

    @Test
    void testPasswordEncoding() {
        String rawPassword = "mySecret123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
    }
}
