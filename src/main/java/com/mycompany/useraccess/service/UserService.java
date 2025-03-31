package com.mycompany.useraccess.service;

import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.exception.EmailAlreadyExistsException;
import com.mycompany.useraccess.exception.SpecializationRequiredException;
import com.mycompany.useraccess.model.RolePermission;
import com.mycompany.useraccess.model.User;
import com.mycompany.useraccess.repository.RolePermissionRepository;
import com.mycompany.useraccess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO dto) throws AccessDeniedException {
        // 1. Email must be unique
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // 2. If DOCTOR, specialization must be provided
        if (dto.getRole() == Role.DOCTOR &&
                (dto.getSpecialization() == null || dto.getSpecialization().isBlank())) {
            throw new SpecializationRequiredException("Specialization is required for role DOCTOR");
        }

        // 3. Check if this is the first user
        boolean isFirstUser = userRepository.count() == 0;

        // 4. Determine role to assign
        Role requestedRole = dto.getRole() != null ? dto.getRole() : Role.CUSTOMER;
        Role roleToAssign = isFirstUser ? Role.ADMIN : requestedRole;
        boolean isSuperAdmin = isFirstUser;

        // 5. Only enforce role hierarchy if not first user
        if (!isFirstUser) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName(); // Email as principal

            User currentUser = userRepository.findByEmail(currentEmail)
                                             .orElseThrow(() -> new UsernameNotFoundException("Logged-in user not found"));

            if (!currentUser.isSuperAdmin()) {
                // Fetch role permissions where canCreate is true
                List<RolePermission> permissions = rolePermissionRepository
                        .findByCreatorRoleRoleAndCanCreateTrue(currentUser.getRole().name());

                boolean canCreate = permissions.stream()
                                               .anyMatch(p -> p.getCreatableRole() != null
                                                       && p.getCreatableRole().getRole() != null
                                                       && p.getCreatableRole().getRole() == roleToAssign);

                if (!canCreate) {
                    throw new AccessDeniedException("You are not allowed to create users with role: " + roleToAssign);
                }
            }
        }

        // 6. DOCTOR must have specialization again (redundant check)
        if (roleToAssign == Role.DOCTOR &&
                (dto.getSpecialization() == null || dto.getSpecialization().isBlank())) {
            throw new SpecializationRequiredException("Specialization is required for role DOCTOR");
        }

        // 7. Create and save the user
        User user = User.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .phone(dto.getPhone())
                        .address(dto.getAddress())
                        .role(roleToAssign)
                        .specialization(dto.getSpecialization())
                        .enabled(false)
                        .superAdmin(isSuperAdmin)
                        .build();

        userRepository.save(user);

        // 8. Send verification email
        emailService.sendVerificationEmail(user);

        return new RegistrationResponseDTO("Registration successful. Please verify your email.", true);
    }

    public boolean activateUser(String token) {
        // Token decode logic here (customize as needed)
        String email = decodeToken(token); // stub
        User user = userRepository.findByEmail(email)
                                  .orElseThrow(() -> new RuntimeException("Invalid activation link"));

        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    private String decodeToken(String token) {
        // Replace with real decoding logic
        return token;
    }
}
