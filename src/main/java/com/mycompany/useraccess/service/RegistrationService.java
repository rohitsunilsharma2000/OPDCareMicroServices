package com.mycompany.useraccess.service;

import com.mycompany.useraccess.context.AppContextHolder;
import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.repository.UserRepository;
import com.mycompany.useraccess.service.strategy.RegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Handles user registration logic by delegating to appropriate {@link RegistrationStrategy} based on role and app type.
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *     <li>Resolves strategy dynamically based on role and app type.</li>
 *     <li>Manages {@link AppContextHolder} lifecycle during registration.</li>
 *     <li>Provides support for one-time SUPER_ADMIN setup via user count.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    /**
     * All available registration strategies for different roles and application types.
     *
     * <p><strong>Constraints:</strong> Each strategy must implement {@link RegistrationStrategy} and declare supported types.</p>
     */
    private final List<RegistrationStrategy> strategies;

    /**
     * Repository used to check user count and store new users.
     */
    private final UserRepository userRepository;

    /**
     * Registers a user based on their role and application type using a matched {@link RegistrationStrategy}.
     *
     * @param dto The user registration data
     *            <ul>
     *                <li><strong>Acceptable Values:</strong> Must include a valid {@link Role} and {@link AppType}.</li>
     *            </ul>
     * @return A DTO with a success message and registration status
     * @throws AccessDeniedException If the registration action is not permitted for the current context
     * @throws UnsupportedOperationException If no strategy supports the given role/app combination
     */
    public RegistrationResponseDTO register(RegistrationRequestDTO dto) throws AccessDeniedException {
        AppType appType = dto.getAppType();
        Role role = dto.getRole();

        // Set the application context before execution
        AppContextHolder.setAppType(appType);

        try {
            // Resolve appropriate strategy
            RegistrationStrategy strategy = strategies.stream()
                                                      .filter(s -> s.supports(appType, role))
                                                      .findFirst()
                                                      .orElseThrow(() -> new UnsupportedOperationException(
                                                              "No strategy found for role " + role + " in app " + appType));

            // Delegate to the resolved strategy
            return strategy.register(dto);

        } catch (AccessDeniedException e) {
            // Forward access denied for Spring Security to handle as 403
            throw e;
        } finally {
            // Always clear the context to avoid leaks in multi-threaded environments
            AppContextHolder.clear();
        }
    }

    /**
     * Returns the total number of registered users.
     *
     * @return Total user count in the system.
     */
    public long count() {
        return userRepository.count();
    }
}
