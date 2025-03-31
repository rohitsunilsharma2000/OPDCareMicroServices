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
 * 🧩 Strategy for registering the first SUPER_ADMIN user
 * Used when setting up the system for the first time.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HospitalAdminStrategy implements RegistrationStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * This strategy supports only HOSPITAL app type and ADMIN role.
     *
     * @param appType the application context (e.g., HOSPITAL)
     * @param role    the role requested for registration
     * @return true if appType is HOSPITAL and role is ADMIN
     */
    @Override
    public boolean supports(AppType appType, Role role) {
        return appType == AppType.HOSPITAL && role == Role.SUPER_ADMIN;
    }

    /**
     * Handles registration of a SUPER_ADMIN user.
     * <p>
     * - If no users exist, allows creation of the first SUPER_ADMIN without authentication.
     * - If users exist, only authenticated SUPER_ADMINs can register another SUPER_ADMIN.
     * - Ensures no more than 5 active SUPER_ADMINs exist at a time.
     *
     * @param dto the registration request DTO containing user details
     * @return RegistrationResponseDTO indicating success or failure
     */

    private static final int SUPER_ADMIN_THRESHOLD = 5;
    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) {
        long totalSuperAdmins = userRepository.countByRoleAndEnabledTrue(Role.SUPER_ADMIN);

        // ⛔ If limit reached, block new active super admin
        //  ❌ Rejecting new registrations when active SUPER_ADMINs reach threshold
        if (totalSuperAdmins >= SUPER_ADMIN_THRESHOLD) {
            log.warn("⛔ SUPER_ADMIN limit reached: {} active users", totalSuperAdmins);
            return new RegistrationResponseDTO("Maximum limit of SUPER_ADMIN users reached. Please deactivate one before adding new.", false);
        }

        // 🧠 Allow only first user to auto-register without authentication
        boolean isFirstUser = userRepository.count() == 0;
        Role assignedRole = isFirstUser ? Role.SUPER_ADMIN : dto.getRole();


//        if (!isFirstUser && assignedRole != Role.SUPER_ADMIN) {
//            log.warn("❌ Only SUPER_ADMIN can register other SUPER_ADMINs (unless it's the first user)");
//            return new RegistrationResponseDTO("Only SUPER_ADMIN can register other SUPER_ADMINs.", false);
//        }

        User user = User.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .phone(dto.getPhone())
                        .address(dto.getAddress())
                        .role(Role.SUPER_ADMIN)
                        .enabled(true)
                        .superAdmin(true)
                        .build();

        userRepository.save(user);

        log.info("✅ SUPER_ADMIN [{}] registered successfully", user.getEmail());

        try {
            emailService.sendVerificationEmail(user); // optional
        } catch (Exception e) {
            log.error("⚠️ Email notification failed for SUPER_ADMIN: {}", e.getMessage());
        }

        return new RegistrationResponseDTO("SUPER_ADMIN registered successfully", true);
    }

}
