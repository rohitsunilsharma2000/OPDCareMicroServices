package com.example.supportservice.controller;




import com.example.supportservice.dto.*;
import com.example.supportservice.model.User;
import com.example.supportservice.service.UserService;
import com.example.supportservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Registers a new user with encoded password and role.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("Registering user: {}", request.getEmail());
        User user = userService.registerUser(request);
        return ResponseEntity.ok("User registered with ID: " + user.getId());
    }

    /**
     * Authenticates a user and returns a JWT token with user info.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for: {}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtUtil.generateToken(request.getUsername(), Map.of());
        User user = userService.getUserDetailsByEmail(request.getUsername());

        UserResponseDTO userResponse = UserResponseDTO.builder()
                                                      .id(user.getId())
                                                      .username(user.getUsername())
                                                      .email(user.getEmail())
                                                      .enabled(user.isEnabled())
                                                      .role(user.getRole() != null ? user.getRole().name() : null)
                                                      .token(token)
                                                      .build();

        return ResponseEntity.ok(userResponse);
    }

    /**
     * Returns profile info of logged-in user.
     */
    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        String email = principal.getName();
        log.info("Fetching profile for: {}", email);
        User user = userService.getUserDetailsByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Updates profile info of logged-in user.
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UserDto userDto) {
        String email = principal.getName();
        log.info("Updating profile for: {}", email);
        User updated = userService.updateUserProfile(email, userDto);
        return ResponseEntity.ok(updated);
    }
}

