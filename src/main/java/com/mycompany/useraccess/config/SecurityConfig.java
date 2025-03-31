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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;

    @Autowired
    private final CustomUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.cors() //  <--- Enable CORS
            .and()
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/v3/api-docs/**" , "/swagger-ui/**").permitAll()

            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/login", "/api/activate").permitAll()
            //            .requestMatchers( "/api/register/setup").permitAll()


            // 🔐 Role-specific registration endpoints
            .requestMatchers(HttpMethod.POST, "/api/register").hasRole("SUPER_ADMIN") // Registers: DOCTOR, NURSE, RECEPTIONIST, SUPER_ADMIN
            .requestMatchers(HttpMethod.POST, "/api/register/patient").hasAnyRole("SUPER_ADMIN", "RECEPTIONIST") // Registers: PATIENT


            // 🔐 Role-specific dashboards or feature access
            .requestMatchers("/api/admin/**").hasRole("SUPER_ADMIN")
            .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
            .requestMatchers("/api/nurse/**").hasRole("NURSE")
            .requestMatchers("/api/receptionist/**").hasRole("RECEPTIONIST")
            .requestMatchers("/api/patient/**").hasRole("PATIENT")

            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, () -> userDetailsService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();

        // Set your allowed origins (or "*", but not recommended in production)
//        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedOriginPatterns(List.of("*")); // for dev

        // Set allowed methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Set allowed headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials if needed
        configuration.setAllowCredentials(true);

        // Preflight cache duration
        configuration.setMaxAge(3600L);

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static final String[] SUPER_ADMIN_ONLY_ENDPOINTS = {
            "/api/register-doctor",
            "/api/register-receptionist",
            "/api/register-nurse",
            "/admin/**"
    };

    private static final String[] DOCTOR_ENDPOINTS = {
            "/doctor/**"
    };

    private static final String[] NURSE_ENDPOINTS = {
            "/nurse/**"
    };

    private static final String[] RECEPTIONIST_ENDPOINTS = {
            "/receptionist/**"
    };

    private static final String[] PATIENT_ENDPOINTS = {
            "/patient/**"
    };

    private static final String[] PATIENT_REGISTRATION_ENDPOINTS = {
            "/api/register-patient"
    };

}