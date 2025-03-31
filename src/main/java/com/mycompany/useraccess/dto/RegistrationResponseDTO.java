package com.mycompany.useraccess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponseDTO {
    private String message;
    private boolean success;
}
