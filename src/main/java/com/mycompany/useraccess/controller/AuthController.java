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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import static com.mycompany.useraccess.utils.JwtUtil.getCurrentEmail;

/**
 * Controller that handles user authentication and administrative account operations.
 *
 * <p><strong>Overview:</strong></p>
 * Provides login functionality using Spring Security, and allows administrators to
 * approve, disable, and list user accounts. JWT tokens are generated on successful login.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *     <li><code>POST /api/login</code> - Authenticate and return JWT token.</li>
 *     <li><code>PUT /api/admin/approve-user/{id}</code> - Admin approves a user account.</li>
 *     <li><code>PUT /api/admin/disable-user/{id}</code> - Admin disables a user account.</li>
 *     <li><code>GET /api/admin/users</code> - List users with optional approval status filtering.</li>
 * </ul>
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Authentication manager for handling user authentication requests.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Utility class for generating and parsing JWT tokens.
     */
    private final JwtUtil jwtUtil;

    /**
     * Repository for accessing and managing user entities.
     */
    private final UserRepository userRepository;

    /**
     * Service for writing entries to the audit log.
     */
    private final AuditLogService auditLogService;

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param loginRequest The login request payload containing username and password.
     *                     <ul>
     *                         <li><strong>Acceptable Values:</strong> Non-null, valid credentials.</li>
     *                     </ul>
     * @return A {@link LoginResponseDTO} containing JWT token and login status.
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *     <li>Invalid credentials result in {@link org.springframework.security.authentication.BadCredentialsException}.</li>
     *     <li>Disabled user accounts receive a 403 response.</li>
     * </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Fetch user entity after authentication
        User user = userRepository.findByUsername(loginRequest.getUsername())
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if account is activated
        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(new LoginResponseDTO(null, "Account not activated. Check email."));
        }

        // Generate JWT token with user's role as a claim
        String jwt = jwtUtil.generateToken(
                user.getEmail(),
                Map.of("role", user.getRole().name())
        );

        return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful"));
    }

    /**
     * Approves a user account by setting the account as enabled and approved.
     *
     * @param id The user ID to approve.
     *           <ul>
     *               <li><strong>Acceptable Values:</strong> Positive long value representing a valid user ID.</li>
     *           </ul>
     * @return A success message upon approval.
     * @throws AccessDeniedException If the current user is not authorized (handled at filter level).
     */
    @PutMapping("/admin/approve-user/{id}")
    public ResponseEntity<?> approveUser(@PathVariable Long id) throws AccessDeniedException {
        // Load user by ID
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update user status
        user.setEnabled(true);
        user.setApprovalStatus("APPROVED");
        userRepository.save(user);

        // Log the approval action
        auditLogService.log(getCurrentEmail(), "APPROVED_USER", "User", id.toString(), "User approved");

        return ResponseEntity.ok("User approved successfully");
    }

    /**
     * Disables an existing user account.
     *
     * @param id The user ID to disable.
     *           <ul>
     *               <li><strong>Acceptable Values:</strong> Must refer to an existing user.</li>
     *           </ul>
     * @return A success message upon disabling the user.
     * @throws AccessDeniedException If access is not permitted.
     */
    @PutMapping("/admin/disable-user/{id}")
    public ResponseEntity<?> disableUser(@PathVariable Long id) throws AccessDeniedException {
        // Find the user by ID
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Disable the user
        user.setEnabled(false);
        userRepository.save(user);

        // Log the disable action
        auditLogService.log(getCurrentEmail(), "DISABLED_USER", "User", id.toString(), "User disabled");

        return ResponseEntity.ok("User disabled successfully");
    }

    /**
     * Lists all users or filters them by approval status.
     *
     * @param approved Optional filter to show only approved or pending users.
     *                 <ul>
     *                     <li><strong>Acceptable Values:</strong> {@code true} for approved, {@code false} for pending, or {@code null} for all.</li>
     *                 </ul>
     * @return A list of users matching the filter criteria.
     */
    @GetMapping("/admin/users")
    public List<User> listUsers(@RequestParam(required = false) Boolean approved) {
        // Filter by approval status if provided
        if (approved != null) {
            return userRepository.findByApprovalStatus(approved ? "APPROVED" : "PENDING");
        }

        // Otherwise, return all users
        return userRepository.findAll();
    }
}
