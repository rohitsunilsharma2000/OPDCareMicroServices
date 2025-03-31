package com.mycompany.useraccess.service.strategy;


import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.model.User;
import com.mycompany.useraccess.repository.UserRepository;
import com.mycompany.useraccess.service.EmailService;
import org.springframework.stereotype.Component;

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
public class HospitalDoctorStrategy implements RegistrationStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public boolean supports(AppType appType, Role role) {
        return appType == AppType.HOSPITAL && role == Role.DOCTOR;
    }

    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) throws AccessDeniedException {
    //        // 🔒 Specialization is mandatory
    //        if (dto.getSpecialization() == null || dto.getSpecialization().isBlank()) {
    //            log.warn("❌ Doctor registration failed: specialization is required");
    //            return new RegistrationResponseDTO("Specialization is required for doctor registration.", false);
    //        }
    //
    //        // ❌ Email must be unique
    //        if (userRepository.existsByEmail(dto.getEmail())) {
    //            log.warn("❌ Registration failed: email already exists - {}", dto.getEmail());
    //            return new RegistrationResponseDTO("Email already exists.", false);
    //        }

        // 🧠 Determine creator
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String createdByEmail = "SELF_REGISTERED";
        User creator = null;

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            createdByEmail = auth.getName();
            creator = userRepository.findByEmail(createdByEmail)
                                    .orElseThrow(() -> new UsernameNotFoundException("Creator not found"));
        }



//        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
//            log.warn("🚫 Unauthenticated attempt to register DOCTOR");
//            throw new AccessDeniedException("Authentication required to register staff users.");
//        }
//        // 🛑 Enforce that only SUPER_ADMIN can create staff
//        if (creator != null && creator.getRole() != Role.SUPER_ADMIN) {
//            log.warn("🚫 Access Denied: [{}] attempted to register DOCTOR", createdByEmail);
//            throw new AccessDeniedException("Only SUPER_ADMIN can register staff users.");
//        }



        // 🏥 Create Doctor user in 'PENDING' state
        User doctor = User.builder()
                          .username(dto.getUsername())
                          .email(dto.getEmail())
                          .password(passwordEncoder.encode(dto.getPassword()))
                          .phone(dto.getPhone())
                          .role(Role.DOCTOR)
                          .specialization(dto.getSpecialization())
                          .departmentId(dto.getDepartmentId()) // departmentId can be null
                          .enabled(false)
                          .approvalStatus("PENDING")
                          .createdBy(createdByEmail)
                          .build();

        userRepository.save(doctor);

        log.info("📝 Doctor registration submitted by [{}] for [{}]", createdByEmail, doctor.getEmail());

        try {
            emailService.notifyAdminsOfPendingApproval(doctor);
        } catch (Exception e) {
            log.error("⚠️ Failed to notify SUPER_ADMINs: {}", e.getMessage());
        }

        return new RegistrationResponseDTO(
                "Doctor registration submitted successfully. Awaiting SUPER_ADMIN approval.",
                true
        );
    }
}
