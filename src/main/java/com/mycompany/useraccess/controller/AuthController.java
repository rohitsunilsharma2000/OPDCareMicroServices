package com.mycompany.useraccess.controller;


import com.mycompany.useraccess.dto.LoginRequestDTO;
import com.mycompany.useraccess.dto.LoginResponseDTO;
import com.mycompany.useraccess.exception.ResourceNotFoundException;
import com.mycompany.useraccess.model.User;
import com.mycompany.useraccess.repository.UserRepository;
import com.mycompany.useraccess.service.AuditLogService;
import com.mycompany.useraccess.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import static com.mycompany.useraccess.utils.JwtUtil.getCurrentEmail;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private final AuditLogService auditLogService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login( @Valid @RequestBody LoginRequestDTO loginRequest) {
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Load user details from DB
        User user = userRepository.findByUsername(loginRequest.getUsername())
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(new LoginResponseDTO(null, "Account not activated. Check email."));
        }

        // Generate JWT
        String jwt = jwtUtil.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));
        return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful"));
    }

    @PutMapping("/admin/approve-user/{id}")
    public ResponseEntity<?> approveUser(@PathVariable Long id) throws AccessDeniedException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEnabled(true);
        user.setApprovalStatus("APPROVED");
        userRepository.save(user);
        auditLogService.log(getCurrentEmail(), "APPROVED_USER", "User", id.toString(), "User approved");
        return ResponseEntity.ok("User approved successfully");
    }
    @PutMapping("/admin/disable-user/{id}")
    public ResponseEntity<?> disableUser(@PathVariable Long id) throws AccessDeniedException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
        auditLogService.log(getCurrentEmail(), "DISABLED_USER", "User", id.toString(), "User disabled");
        return ResponseEntity.ok("User disabled successfully");
    }
    @GetMapping("/admin/users")
    public List<User> listUsers( @RequestParam(required = false) Boolean approved) {
        if (approved != null) {
            return userRepository.findByApprovalStatus(approved ? "APPROVED" : "PENDING");
        }
        return userRepository.findAll();
    }

//    // ✅ Accessible only by DOCTOR
//    @PreAuthorize("hasRole('DOCTOR')")
//    @GetMapping("/doctor/patients")
//    public ResponseEntity<String> doctorAccessTest() {
//        return ResponseEntity.ok("Access granted: DOCTOR can view assigned patients");
//    }
//
//    // ✅ Accessible only by NURSE
//    @PreAuthorize("hasRole('NURSE')")
//    @GetMapping("/nurse/patients")
//    public ResponseEntity<String> nurseAccessTest() {
//        return ResponseEntity.ok("Access granted: NURSE can view department patients");
//    }
//
//    // ✅ Accessible only by RECEPTIONIST
//    @PreAuthorize("hasRole('RECEPTIONIST')")
//    @PostMapping("/receptionist/patients")
//    public ResponseEntity<String> receptionistPatientRegistration() {
//        return ResponseEntity.ok("Access granted: RECEPTIONIST can register new patients");
//    }
//
//    // ✅ Accessible only by PATIENT
//    @PreAuthorize("hasRole('PATIENT')")
//    @GetMapping("/patients/me")
//    public ResponseEntity<String> patientProfileAccess() {
//        return ResponseEntity.ok("Access granted: PATIENT can view their profile");
//    }
//
//    // ✅ Accessible only by SUPER_ADMIN
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
//    @GetMapping("/admin/dashboard")
//    public ResponseEntity<String> adminDashboardAccess() {
//        return ResponseEntity.ok("Access granted: SUPER_ADMIN can manage users and system settings");
//    }



}
