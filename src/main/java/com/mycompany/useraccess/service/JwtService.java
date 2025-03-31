package com.mycompany.useraccess.service;


import com.mycompany.useraccess.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    public boolean validateToken(String token, String username) {
        return jwtUtil.validateToken(token, username);
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null &&
                username.equals(userDetails.getUsername()) &&
                !jwtUtil.isTokenExpired(token);
    }
}