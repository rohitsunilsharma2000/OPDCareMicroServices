package com.mycompany.useraccess.controller;


import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping("/setup")
    public ResponseEntity<RegistrationResponseDTO> setupSuperAdmin(@RequestBody RegistrationRequestDTO dto) throws AccessDeniedException {
        if (registrationService.count() > 0) {
            throw new AccessDeniedException("Initial setup already completed.");
        }
        dto.setRole(Role.SUPER_ADMIN);
        return ResponseEntity.ok(registrationService.register(dto));
    }


    /**
     * 🔐 SUPER_ADMIN-only registration endpoint
     * Supports: SUPER_ADMIN, DOCTOR, NURSE, RECEPTIONIST, PATIENT
     */
    @PostMapping
    public ResponseEntity<RegistrationResponseDTO> registerAnyStaffOrSuperAdmin(
            @RequestBody RegistrationRequestDTO dto) throws AccessDeniedException {
        return ResponseEntity.ok(registrationService.register(dto));
    }

    /**
     * 👨‍👩‍👧 RECEPTIONIST or SUPER_ADMIN can register PATIENTs
     */
    @PostMapping("/patient")
    public ResponseEntity<RegistrationResponseDTO> registerPatient(
            @RequestBody RegistrationRequestDTO dto) throws AccessDeniedException {
        dto.setRole(Role.PATIENT);
        return ResponseEntity.ok(registrationService.register(dto));
    }
}
