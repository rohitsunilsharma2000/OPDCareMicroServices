package com.example.supportservice.config;

import com.example.supportservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebSocketJwtInterceptorTest {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private WebSocketJwtInterceptor interceptor;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        interceptor = new WebSocketJwtInterceptor(jwtService, userDetailsService);
    }

    @Test
    void testBeforeHandshake_withValidJwt_shouldAuthenticateUser() throws Exception {
        // Arrange
        String jwtToken = "mock-jwt-token";
        String email = "user@example.com";

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);

        UserDetails userDetails = new User(email, "password", Collections.emptyList());
        when(jwtService.extractUsername(jwtToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwtToken, userDetails)).thenReturn(true);

        Map<String, Object> attributes = new HashMap<>();
        WebSocketHandler wsHandler = mock(WebSocketHandler.class);

        // Act
        boolean result = interceptor.beforeHandshake(request, null, wsHandler, attributes);

        // Assert
        assertTrue(result);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
        assertTrue(auth.isAuthenticated());
        assertTrue(auth.getName().equals(email));
    }

    @Test
    void testBeforeHandshake_withInvalidJwt_shouldNotAuthenticate() throws Exception {
        String jwtToken = "invalid-jwt";
        String email = "user@example.com";

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
        UserDetails userDetails = new User(email, "password", Collections.emptyList());

        when(jwtService.extractUsername(jwtToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwtToken, userDetails)).thenReturn(false);

        Map<String, Object> attributes = new HashMap<>();

        boolean result = interceptor.beforeHandshake(request, null, mock(WebSocketHandler.class), attributes);

        assertTrue(result);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
    }

    @Test
    void testBeforeHandshake_withoutAuthorizationHeader_shouldProceedWithoutAuth() throws Exception {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn(null);

        ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
        Map<String, Object> attributes = new HashMap<>();

        boolean result = interceptor.beforeHandshake(request, null, mock(WebSocketHandler.class), attributes);

        assertTrue(result);
    }
}
