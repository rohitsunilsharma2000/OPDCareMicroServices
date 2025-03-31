package com.mycompany.useraccess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representing the result of a registration operation.
 *
 * <p><strong>Usage:</strong> Returned by the registration endpoint.</p>
 */
@Data
@AllArgsConstructor
public class RegistrationResponseDTO {

    /**
     * Message describing the outcome of the registration.
     */
    private String message;

    /**
     * Boolean flag indicating whether registration was successful.
     */
    private boolean success;
}
