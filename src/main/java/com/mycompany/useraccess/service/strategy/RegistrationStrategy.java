package com.mycompany.useraccess.service.strategy;

import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;

import java.nio.file.AccessDeniedException;

/**
 * Strategy interface for user registration based on application type and role.
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *     <li>Each implementing class determines whether it supports a specific app type and role.</li>
 *     <li>Provides logic to register a user with that role in that context.</li>
 * </ul>
 */
public interface RegistrationStrategy {

    /**
     * Checks whether this strategy can handle the given role and application type.
     *
     * @param appType The application context (e.g., HOSPITAL)
     * @param role    The user role to be registered
     * @return true if this strategy supports the combination
     */
    boolean supports(AppType appType, Role role);

    /**
     * Registers a user for the given role and app type.
     *
     * @param dto The registration request DTO
     * @return Response DTO with success/failure message
     * @throws AccessDeniedException if current user is not authorized to perform this registration
     */
    RegistrationResponseDTO register(RegistrationRequestDTO dto) throws AccessDeniedException;
}
