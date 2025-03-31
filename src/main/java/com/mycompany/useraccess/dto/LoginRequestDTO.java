package com.mycompany.useraccess.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for handling login requests.
 *
 * <p><strong>Usage:</strong> Used in authentication endpoint to receive login credentials.</p>
 */
@Data
public class LoginRequestDTO {

    /**
     * Username or email of the user.
     *
     * <p><strong>Constraints:</strong> Cannot be null or empty.</p>
     */
    @NotBlank
    private String username;

    /**
     * User's password.
     *
     * <p><strong>Constraints:</strong> Cannot be null or empty.</p>
     */
    @NotBlank
    private String password;
}
