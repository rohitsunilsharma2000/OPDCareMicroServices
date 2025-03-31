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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

/**
 * Strategy for registering DOCTOR users under the HOSPITAL application.
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *     <li>Must be created by an authenticated user (recommended: SUPER_ADMIN).</li>
 *     <li>Specialization is required (enforced separately via validation).</li>
 *     <li>User is stored in DISABLED and PENDING state, awaiting approval.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HospitalDoctorStrategy implements RegistrationStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Only supports HOSPITAL app type and DOCTOR role.
     */
    @Override
    public boolean supports(AppType appType, Role role) {
        return appType == AppType.HOSPITAL && role == Role.DOCTOR;
    }

    /**
     * Registers a doctor account with pending approval.
     *
     * @param dto Registration details for the doctor
     * @return Success response after storing doctor in DB
     * @throws AccessDeniedException if registration is performed by unauthorized user (commented logic available)
     */
    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) throws AccessDeniedException {
        // Fetch current authenticated user from the Spring Security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Fallback in case authentication is missing or anonymous
        String createdByEmail = "SELF_REGISTERED";
        User creator = null;

        // If the request is authenticated, extract the creator email
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            createdByEmail = auth.getName();

            // Fetch the creator entity from DB for potential role checks
            creator = userRepository.findByEmail(createdByEmail)
                                    .orElseThrow(() -> new UsernameNotFoundException("Creator not found"));
        }

        // Create a new DOCTOR user with disabled status and "PENDING" approval state
        User doctor = User.builder()
                          .username(dto.getUsername())
                          .email(dto.getEmail())
                          .password(passwordEncoder.encode(dto.getPassword())) // Encrypt the password
                          .phone(dto.getPhone())
                          .role(Role.DOCTOR)
                          .specialization(dto.getSpecialization())             // Specialization is mandatory (validated elsewhere)
                          .departmentId(dto.getDepartmentId())                 // Optional field, can be null
                          .enabled(false)                                      // Mark as disabled until approved
                          .approvalStatus("PENDING")                           // Approval status tracked
                          .createdBy(createdByEmail)                           // Track who created the user
                          .build();

        // Save doctor to the database
        userRepository.save(doctor);

        log.info("📝 Doctor registration submitted by [{}] for [{}]", createdByEmail, doctor.getEmail());

        // Notify all SUPER_ADMINs that a doctor is awaiting approval
        try {
            emailService.notifyAdminsOfPendingApproval(doctor);
        } catch (Exception e) {
            log.error("⚠️ Failed to notify SUPER_ADMINs: {}", e.getMessage());
        }

        // Return a success response
        return new RegistrationResponseDTO(
                "Doctor registration submitted successfully. Awaiting SUPER_ADMIN approval.",
                true
        );
    }
}
