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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * Strategy for registering SUPER_ADMIN users under the HOSPITAL application.
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *     <li>Allows initial setup without authentication.</li>
 *     <li>Caps active SUPER_ADMIN count to 5 for safety.</li>
 *     <li>Auto-enables the SUPER_ADMIN upon registration.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HospitalAdminStrategy implements RegistrationStrategy {
    /**
     * Maximum number of active SUPER_ADMINs allowed at a time.
     */
    private static final int SUPER_ADMIN_THRESHOLD = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * This strategy applies to HOSPITAL applications for SUPER_ADMIN role only.
     */
    @Override
    public boolean supports(AppType appType, Role role) {
        return appType == AppType.HOSPITAL && role == Role.SUPER_ADMIN;
    }

    /**
     * Handles registration of a SUPER_ADMIN user.
     *
     * @param dto the registration request DTO containing user details
     * @return RegistrationResponseDTO indicating success or failure
     */
    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) {
        // Get count of currently active (enabled) SUPER_ADMIN users
        long totalSuperAdmins = userRepository.countByRoleAndEnabledTrue(Role.SUPER_ADMIN);

        // If the number of active super admins reaches the threshold, block the request
        if (totalSuperAdmins >= SUPER_ADMIN_THRESHOLD) {
            log.warn("⛔ SUPER_ADMIN limit reached: {} active users", totalSuperAdmins);
            return new RegistrationResponseDTO(
                    "Maximum limit of SUPER_ADMIN users reached. Please deactivate one before adding new.",
                    false
            );
        }

        // Check if this is the first ever user being registered
        boolean isFirstUser = userRepository.count() == 0;

        // Always register SUPER_ADMIN regardless of what was passed in the request
        // In real scenarios, we may want to assert dto.getRole() == SUPER_ADMIN here
        User user = User.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword())) // Securely hash the password
                        .phone(dto.getPhone())
                        .address(dto.getAddress())
                        .role(Role.SUPER_ADMIN)
                        .enabled(true)          // Immediately enable SUPER_ADMIN
                        .superAdmin(true)       // Mark as a super admin for internal reference
                        .build();

        // Persist user to the database
        userRepository.save(user);

        log.info("✅ SUPER_ADMIN [{}] registered successfully", user.getEmail());

        // Send optional verification email (can be removed if not needed)
        try {
            emailService.sendVerificationEmail(user);
        } catch (Exception e) {
            log.error("⚠️ Email notification failed for SUPER_ADMIN: {}", e.getMessage());
        }

        // Return success response
        return new RegistrationResponseDTO("SUPER_ADMIN registered successfully", true);
    }
}
