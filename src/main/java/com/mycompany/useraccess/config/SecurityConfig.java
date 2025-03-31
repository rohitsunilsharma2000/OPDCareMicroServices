package com.mycompany.useraccess.config;

import com.mycompany.useraccess.filter.JwtAuthenticationFilter;
import com.mycompany.useraccess.service.CustomUserDetailsService;
import com.mycompany.useraccess.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Security configuration class that sets up JWT-based authentication and role-based access control.
 *
 * <p><strong>Overview:</strong></p>
 * Defines access permissions for various API endpoints, configures password encoding,
 * registers JWT filter, handles authentication and authorization exceptions, and applies global CORS policy.
 *
 * <p><strong>Security Mechanisms:</strong></p>
 * <ul>
 *     <li>JWT for stateless authentication</li>
 *     <li>Role-based endpoint protection</li>
 *     <li>Custom JSON responses for access errors</li>
 *     <li>CORS configuration for frontend-backend communication</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * JWT service for token creation and validation.
     *
     * <p><strong>Constraints:</strong> Must not be null; injected by Spring via constructor.</p>
     */
    private final JwtService jwtService;

    /**
     * Custom implementation of {@link UserDetailsService} to load user details from DB.
     *
     * <p><strong>Constraints:</strong> Spring-managed component, must be injected.</p>
     */
    @Autowired
    private final CustomUserDetailsService userDetailsService;

    /**
     * Configures security filters and role-based access rules for HTTP requests.
     *
     * @param http                   Spring Security configuration builder.
     *                               <ul>
     *                                   <li><strong>Acceptable Values:</strong> Should be configured using standard HTTP security methods.</li>
     *                               </ul>
     * @param jwtAuthenticationFilter JWT filter for validating access tokens.
     *                                <ul>
     *                                    <li><strong>Acceptable Values:</strong> Must be a valid implementation of {@link JwtAuthenticationFilter}.</li>
     *                                </ul>
     * @return Configured {@link SecurityFilterChain} applied to all HTTP requests.
     * @throws Exception If any misconfiguration occurs during filter setup.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .cors() // Enable CORS globally
                .and()
                .csrf().disable() // Disable CSRF protection as JWT ensures stateless auth
                .authorizeHttpRequests()

                // Publicly accessible endpoints
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/login", "/api/activate").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/register/setup").permitAll()

                // Restricted endpoints by role
                .requestMatchers(HttpMethod.POST, "/api/register").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/register/patient").hasAnyRole("SUPER_ADMIN", "RECEPTIONIST")
                .requestMatchers("/api/admin/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/api/nurse/**").hasRole("NURSE")
                .requestMatchers("/api/receptionist/**").hasRole("RECEPTIONIST")
                .requestMatchers("/api/patient/**").hasRole("PATIENT")

                // All other requests must be authenticated
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Custom error response for 403 - Access Denied
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now() + "\","
                                                       + "\"message\":\"Access Denied\","
                                                       + "\"status\":403,"
                                                       + "\"error\":\"Forbidden\","
                                                       + "\"path\":\"" + request.getRequestURI() + "\"}");
                })

                // Custom error response for 401 - Unauthorized
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now() + "\","
                                                       + "\"message\":\"Unauthorized\","
                                                       + "\"status\":401,"
                                                       + "\"error\":\"Unauthorized\","
                                                       + "\"path\":\"" + request.getRequestURI() + "\"}");
                });

        return http.build();
    }

    /**
     * Initializes and registers a {@link JwtAuthenticationFilter} bean for JWT token validation.
     *
     * @param userDetailsService Custom user loader used during authentication.
     *                           <ul>
     *                               <li><strong>Acceptable Values:</strong> Must provide valid user details implementation.</li>
     *                           </ul>
     * @return Instance of {@link JwtAuthenticationFilter}.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, () -> userDetailsService);
    }

    /**
     * Registers {@link AuthenticationProvider} using {@link DaoAuthenticationProvider} and BCrypt encoder.
     *
     * @param userDetailsService User service for loading credentials.
     *                           <ul>
     *                               <li><strong>Acceptable Values:</strong> Custom implementation backed by DB.</li>
     *                           </ul>
     * @return A fully configured authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Provides a {@link PasswordEncoder} bean using BCrypt hashing algorithm.
     *
     * @return A secure BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an {@link AuthenticationManager} for handling authentication flow.
     *
     * @param authenticationConfiguration Spring Boot's built-in authentication manager configuration.
     *                                   <ul>
     *                                       <li><strong>Acceptable Values:</strong> Autowired by Spring context.</li>
     *                                   </ul>
     * @return Auth manager to be used by login services.
     * @throws Exception If an error occurs during creation.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures a global CORS policy for the application.
     *
     * <p><strong>Description:</strong> Allows all origins, methods, and headers in development environment.
     * Update origin patterns for production to avoid security risks.</p>
     *
     * @return Configured CORS policy applied to all endpoints.
     */
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*")); // Allow all origins (use restricted origins in prod)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow common HTTP methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies / Authorization headers
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }
}
