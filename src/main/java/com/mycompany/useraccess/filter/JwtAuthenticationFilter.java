package com.mycompany.useraccess.filter;

import com.mycompany.useraccess.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * JWT Authentication Filter that processes and validates JWT tokens for every HTTP request.
 *
 * <p><strong>Overview:</strong></p>
 * This filter runs once per request. It extracts the JWT token from the `Authorization` header,
 * validates it, and sets up the Spring Security context if the token is valid.
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *     <li>Intercepts requests and extracts Bearer tokens.</li>
 *     <li>Delegates to {@link JwtService} for validation and username extraction.</li>
 *     <li>If valid, loads user details and populates Spring Security context.</li>
 *     <li>Returns a 401 response if the token is invalid or expired.</li>
 * </ul>
 *
 * <p><strong>Security Note:</strong> This filter ensures only authenticated users can access protected resources.</p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Service to handle token creation, extraction, and validation.
     *
     * <p><strong>Constraints:</strong> Must be injected and not null.</p>
     */
    private final JwtService jwtService;

    /**
     * Supplier for loading {@link UserDetailsService}, used for lazy initialization.
     *
     * <p><strong>Constraints:</strong> Should return a non-null implementation of {@link UserDetailsService}.</p>
     */
    private final Supplier<UserDetailsService> userDetailsServiceSupplier;

    /**
     * Intercepts incoming HTTP requests and authenticates based on JWT.
     *
     * @param request     Incoming HTTP request containing Authorization header.
     * @param response    HTTP response object used to send 401 status if unauthorized.
     * @param filterChain Filter chain to delegate request processing.
     * @throws ServletException If servlet request processing fails.
     * @throws IOException      If an I/O error occurs during filtering.
     *
     * <p><strong>Acceptable Values:</strong></p>
     * <ul>
     *     <li><code>Authorization: Bearer &lt;token&gt;</code> must be present in header.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *     <li>Missing or malformed Authorization header – filter is skipped.</li>
     *     <li>Invalid or expired token – returns 401 Unauthorized.</li>
     * </ul>
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");

        // If there's no Bearer token, continue the chain without authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer " prefix to get the token
        String token = authHeader.substring(7);
        try {
            // Extract username/email from token
            String username = jwtService.extractUsername(token);

            // Proceed only if username is not null and the user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details using UserDetailsService
                UserDetails userDetails = userDetailsServiceSupplier.get().loadUserByUsername(username);

                // Validate the token against username/email
                if (jwtService.validateToken(token, username)) {
                    // If valid, create authentication token and set it in the security context
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    // If token is invalid (manipulated or expired), throw an exception
                    throw new io.jsonwebtoken.JwtException("Invalid token");
                }
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            // Token validation failed (e.g., expired, malformed)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Unauthorized: Invalid or expired JWT token\"}");
            return; // Skip filter chain to prevent further processing
        }

        // If token is valid or authentication not needed, continue filter chain
        filterChain.doFilter(request, response);
    }
}
