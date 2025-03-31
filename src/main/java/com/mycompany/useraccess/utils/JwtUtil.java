package com.mycompany.useraccess.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Utility class for generating and validating JSON Web Tokens (JWT).
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *     <li>Generate signed JWT tokens with custom claims.</li>
 *     <li>Validate tokens against usernames and expiration.</li>
 *     <li>Extract username (subject) from token.</li>
 *     <li>Resolve current authenticated user from Spring Security context.</li>
 * </ul>
 */
@Component
public class JwtUtil {

    /**
     * Secret signing key for HS256 JWT signature. Generated at runtime.
     * For production, this should be externalized and stored securely (e.g., in a Vault or config server).
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token for the given username and optional claims.
     *
     * @param username the subject (user's email or username)
     * @param claims   a map of additional claims (e.g., role)
     * @return a signed JWT string
     */
    public String generateToken(String username, Map<String, Object> claims) {
        if (claims == null) {
            claims = Map.of(); // Ensure no null claims map
        }

        // Create JWT with subject, claims, issue time, and 10-hour expiration
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                   .signWith(key) // Sign using HS256 and the secret key
                   .compact();
    }

    /**
     * Validates a JWT token by checking the username and expiration.
     *
     * @param token    the token string to validate
     * @param username the expected username (subject)
     * @return true if valid and not expired
     */
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Extracts the username (subject) from the token.
     *
     * @param token the JWT token
     * @return the subject (username or email)
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT token
     * @return true if token expiration date is before current time
     */
    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getExpiration()
                   .before(new Date());
    }

    /**
     * Returns the email/username of the currently authenticated user.
     *
     * @return the principal name from Spring Security context
     * @throws AccessDeniedException if user is not authenticated
     */
    public static String getCurrentEmail() throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Ensure the user is authenticated and not anonymous
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }

        // Unauthorized access
        throw new AccessDeniedException("Unauthenticated access");
    }
}
