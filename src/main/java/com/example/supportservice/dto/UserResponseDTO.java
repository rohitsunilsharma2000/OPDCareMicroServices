package com.example.supportservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean enabled;
    private String token;
}