package com.example.supportservice.utils;


import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    public void testGenerateAndValidateToken() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email, Map.of());

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, email));
        assertEquals(email, jwtUtil.extractUsername(token));
    }
}
