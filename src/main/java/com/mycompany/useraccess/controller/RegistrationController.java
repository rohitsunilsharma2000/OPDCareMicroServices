package com.mycompany.useraccess.controller;

import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

/**
 * Controller for handling user registration operations across different roles.
 *
 * <p><strong>Overview:</strong></p>
 * Provides role-based registration functionality including initial setup for SUPER_ADMIN
 * and separate endpoints for registering staff and patients.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *     <li><code>POST /api/register/setup</code> - Registers the first SUPER_ADMIN (one-time setup).</li>
 *     <li><code>POST /api/register</code> - Registers staff or SUPER_ADMIN (requires admin privileges).</li>
 *     <li><code>POST /api/register/patient</code> - Registers a PATIENT (allowed by SUPER_ADMIN or RECEPTIONIST).</li>
 * </ul>
 *
 * <p><strong>Security:</strong> Role-based access control is enforced in the security config.</p>
 */
@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    /**
     * Service layer responsible for handling registration logic.
     *
     * <p><strong>Constraints:</strong> Must be a Spring-managed singleton bean.</p>
     */
    private final RegistrationService registrationService;

    /**
     * Endpoint to perform one-time initial setup by registering the first SUPER_ADMIN user.
     *
     * <p><strong>Description:</strong></p>
     * Allows creation of the initial SUPER_ADMIN only if no users exist in the system.
     * Prevents multiple initial setups for security reasons.
     *
     * @param dto The registration request containing user details.
     *            <ul>
     *                <li><strong>Acceptable Values:</strong> Must contain valid username, email, and password.</li>
     *            </ul>
     * @return A response containing the registered SUPER_ADMIN's information.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *     <li>Throws {@link AccessDeniedException} if setup has already been completed.</li>
     * </ul>
     */
    @PostMapping("/setup")
    public ResponseEntity<RegistrationResponseDTO> setupSuperAdmin(@RequestBody RegistrationRequestDTO dto) throws AccessDeniedException {
        // Only allow one SUPER_ADMIN to be created during setup
        if (registrationService.count() > 0) {
            throw new AccessDeniedException("Initial setup already completed.");
        }

        // Force role to SUPER_ADMIN regardless of input
        dto.setRole(Role.SUPER_ADMIN);

        return ResponseEntity.ok(registrationService.register(dto));
    }

    /**
     * Registers staff users like DOCTOR, NURSE, RECEPTIONIST, or another SUPER_ADMIN.
     *
     * <p><strong>Description:</strong></p>
     * Restricted to users with administrative privileges. Role is passed in the DTO.
     *
     * @param dto The registration data containing user role, username, email, and password.
     *            <ul>
     *                <li><strong>Acceptable Values:</strong> Role must be a valid non-PATIENT role.</li>
     *            </ul>
     * @return The registration response DTO with user details and status.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *     <li>Invalid role or missing fields in DTO will result in validation or business exceptions.</li>
     *     <li>Access is denied to non-admin users (enforced via Spring Security).</li>
     * </ul>
     */
    @PostMapping
    public ResponseEntity<RegistrationResponseDTO> registerAnyStaffOrSuperAdmin(
            @RequestBody RegistrationRequestDTO dto) throws AccessDeniedException {
        return ResponseEntity.ok(registrationService.register(dto));
    }

    /**
     * Registers a patient account. Allowed for RECEPTIONIST or SUPER_ADMIN roles.
     *
     * <p><strong>Description:</strong></p>
     * Forces the role in the DTO to {@link Role#PATIENT}, regardless of client input.
     *
     * @param dto The incoming request body for patient registration.
     *            <ul>
     *                <li><strong>Acceptable Values:</strong> Must include name, email, and password. Role is overridden.</li>
     *            </ul>
     * @return A DTO with success message and patient info.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *     <li>Throws exception if required fields are missing.</li>
     *     <li>Access is restricted to authorized roles (checked by Spring Security config).</li>
     * </ul>
     */
    @PostMapping("/patient")
    public ResponseEntity<RegistrationResponseDTO> registerPatient(
            @RequestBody RegistrationRequestDTO dto) throws AccessDeniedException {
        // Ensure only PATIENT role is allowed through this endpoint
        dto.setRole(Role.PATIENT);
        return ResponseEntity.ok(registrationService.register(dto));
    }
}
