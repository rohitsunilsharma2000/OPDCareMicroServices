package com.mycompany.useraccess.service.strategy;

import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.model.User;
import com.mycompany.useraccess.repository.UserRepository;
import com.mycompany.useraccess.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

/**
 * Strategy for registering RECEPTIONIST users under the HOSPITAL application.
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *     <li>Authentication is recommended for tracking creator.</li>
 *     <li>User is created in DISABLED and PENDING state.</li>
 *     <li>Admins are notified via email for approval workflow.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HospitalReceptionistStrategy implements RegistrationStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Only supports HOSPITAL app type and RECEPTIONIST role.
     */
    @Override
    public boolean supports(AppType appType, Role role) {
        return appType == AppType.HOSPITAL && role == Role.RECEPTIONIST;
    }

    /**
     * Registers a receptionist user and sets status to pending.
     *
     * @param dto Registration details for the receptionist
     * @return A response object indicating success/failure
     * @throws AccessDeniedException if registration is blocked (can be enabled via commented logic)
     */
    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) throws AccessDeniedException {
        // Check for existing user by email to prevent duplicates
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("❌ Registration failed: email already exists - {}", dto.getEmail());
            return new RegistrationResponseDTO("Email already exists.", false);
        }

        // Get authentication object from Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Default creator email is "SELF_REGISTERED" for anonymous users
        String createdByEmail = "SELF_REGISTERED";

        // If request is authenticated, capture the authenticated user's email
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            createdByEmail = auth.getName();

            // NOTE: Additional validation such as role check (e.g., only SUPER_ADMIN can register)
            // can be added here if needed. Commented below:
            //
            // User creator = userRepository.findByEmail(createdByEmail)
            //         .orElseThrow(() -> new UsernameNotFoundException("Creator not found"));
            //
            // if (creator.getRole() != Role.SUPER_ADMIN) {
            //     log.warn("🚫 Access Denied: [{}] attempted to register RECEPTIONIST", createdByEmail);
            //     throw new AccessDeniedException("Only SUPER_ADMIN can register staff users.");
            // }
        }

        // Construct a new receptionist user in a disabled and pending state
        User receptionist = User.builder()
                                .username(dto.getUsername())
                                .email(dto.getEmail())
                                .password(passwordEncoder.encode(dto.getPassword())) // Securely hash the password
                                .phone(dto.getPhone())
                                .role(Role.RECEPTIONIST)
                                .enabled(false)                     // User is not active yet
                                .approvalStatus("PENDING")          // Awaiting manual approval
                                .createdBy(createdByEmail)          // Store who registered this user
                                .build();

        // Save the receptionist to the database
        userRepository.save(receptionist);

        log.info("📝 Receptionist registration submitted by [{}] for [{}]", createdByEmail, receptionist.getEmail());

        // Attempt to notify SUPER_ADMINs of the new registration
        try {
            emailService.notifyAdminsOfPendingApproval(receptionist);
        } catch (Exception e) {
            log.error("⚠️ Failed to notify SUPER_ADMINs: {}", e.getMessage());
        }

        // Return a successful response to the client
        return new RegistrationResponseDTO(
                "Receptionist registration submitted successfully. Awaiting SUPER_ADMIN approval.",
                true
        );
    }
}
