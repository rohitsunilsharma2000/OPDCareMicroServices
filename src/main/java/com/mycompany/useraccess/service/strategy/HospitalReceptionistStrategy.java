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

@Component
@RequiredArgsConstructor
@Slf4j
public class HospitalReceptionistStrategy implements RegistrationStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public boolean supports(AppType appType, Role role) {
        return appType == AppType.HOSPITAL && role == Role.RECEPTIONIST;
    }

    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) throws AccessDeniedException {
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("❌ Registration failed: email already exists - {}", dto.getEmail());
            return new RegistrationResponseDTO("Email already exists.", false);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String createdByEmail = "SELF_REGISTERED";
        User creator = null;
//
//        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
//            log.warn("🚫 Unauthenticated attempt to register RECEPTIONIST");
//            throw new AccessDeniedException("Authentication required to register staff users.");
//        }

        createdByEmail = auth.getName();
//        creator = userRepository.findByEmail(createdByEmail)
//                                .orElseThrow(() -> new UsernameNotFoundException("Creator not found"));

//        if (creator.getRole() != Role.SUPER_ADMIN) {
//            log.warn("🚫 Access Denied: [{}] attempted to register RECEPTIONIST", createdByEmail);
//            throw new AccessDeniedException("Only SUPER_ADMIN can register staff users.");
//        }

        User receptionist = User.builder()
                                .username(dto.getUsername())
                                .email(dto.getEmail())
                                .password(passwordEncoder.encode(dto.getPassword()))
                                .phone(dto.getPhone())
                                .role(Role.RECEPTIONIST)
                                .enabled(false)
                                .approvalStatus("PENDING")
                                .createdBy(createdByEmail)
                                .build();

        userRepository.save(receptionist);

        log.info("📝 Receptionist registration submitted by [{}] for [{}]", createdByEmail, receptionist.getEmail());

        try {
            emailService.notifyAdminsOfPendingApproval(receptionist);
        } catch (Exception e) {
            log.error("⚠️ Failed to notify SUPER_ADMINs: {}", e.getMessage());
        }

        return new RegistrationResponseDTO(
                "Receptionist registration submitted successfully. Awaiting SUPER_ADMIN approval.",
                true
        );
    }
}
