package com.mycompany.useraccess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO returned after a successful or failed login attempt.
 *
 * <p><strong>Fields:</strong></p>
 * <ul>
 *     <li><code>token</code> – JWT token for the session.</li>
 *     <li><code>message</code> – Message describing login status.</li>
 * </ul>
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {

    /**
     * JWT access token returned upon successful login.
     *
     * <p><strong>Constraints:</strong> May be null if login failed.</p>
     */
    private String token;

    /**
     * Message describing the login result (success or error reason).
     */
    private String message;
}
