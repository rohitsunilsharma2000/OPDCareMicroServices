package com.example.supportservice.service;


import com.example.supportservice.dto.RegisterRequest;
import com.example.supportservice.dto.UserDto;
import com.example.supportservice.enums.Role;
import com.example.supportservice.exception.ResourceNotFoundException;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User registerUser( RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        user.setEnabled(true);

        User saved = userRepository.save(user);
        log.info("User registered successfully: {}", saved.getId());
        return saved;
    }

    public User getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User updateUserProfile(String email, UserDto dto) {
        User user = getUserDetailsByEmail(email);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}