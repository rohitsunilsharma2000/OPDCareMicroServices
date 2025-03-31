package com.mycompany.useraccess.service;

import com.mycompany.useraccess.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for JWT-related operations such as validation and extraction.
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *     <li>Delegates to {@link JwtUtil} for token parsing and verification.</li>
 *     <li>Used by the JWT filter and authentication process.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    /**
     * Utility class that encapsulates JWT token logic.
     */
    private final JwtUtil jwtUtil;

    /**
     * Validates a JWT token using the expected username.
     *
     * @param token    The JWT token
     * @param username Username or email to validate against
     * @return True if the token is valid and matches the username
     */
    public boolean validateToken(String token, String username) {
        return jwtUtil.validateToken(token, username);
    }

    /**
     * Extracts the username or email from the given JWT.
     *
     * @param token The JWT token
     * @return The username/email if present in token claims
     */
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    /**
     * Validates the token against the user details and checks expiration.
     *
     * @param token        JWT token
     * @param userDetails  Authenticated user details
     * @return True if token is valid, belongs to user, and not expired
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null &&
                username.equals(userDetails.getUsername()) &&
                !jwtUtil.isTokenExpired(token);
    }
}
