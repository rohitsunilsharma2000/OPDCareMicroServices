package com.example.supportservice.service;

import com.example.supportservice.enums.Role;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService();
        userDetailsService.userRepository = userRepository; // field injection for test
    }

    @Test
    void loadUserByUsername_ReturnsUserDetails_WhenUserExists() {
        User user = User.builder()
                        .id(1L)
                        .email("test@example.com")
                        .password("encodedPass")
                        .enabled(true)
                        .role(Role.ADMIN)
                        .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPass");
        assertThat(userDetails.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_ADMIN");

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_ThrowsException_WhenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("notfound@example.com"));

        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }
}
