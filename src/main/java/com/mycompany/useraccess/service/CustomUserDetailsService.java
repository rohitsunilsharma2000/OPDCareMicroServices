package com.mycompany.useraccess.service;

import com.mycompany.useraccess.model.User;
import com.mycompany.useraccess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService} for authentication.
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *     <li>Loads user details by username or email for authentication purposes.</li>
 *     <li>Used by Spring Security during login and JWT validation.</li>
 * </ul>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing user data.
     *
     * <p><strong>Constraints:</strong> Must not be null. Injected by Spring.</p>
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Loads a user by username or email. Used by Spring Security.
     *
     * @param usernameOrEmail Username or email to search for.
     *                        <ul>
     *                            <li><strong>Acceptable Values:</strong> Must match existing username or email.</li>
     *                        </ul>
     * @return UserDetails required by Spring Security
     * @throws UsernameNotFoundException if no user is found with given identifier
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Try finding by username, then fallback to email
        User user = userRepository.findByUsername(usernameOrEmail)
                                  .or(() -> userRepository.findByEmail(usernameOrEmail))
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

        // Return a Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                                                                 .username(user.getEmail()) // Use email as unique identifier
                                                                 .password(user.getPassword())
                                                                 .roles(user.getRole().name())
                                                                 .disabled(!user.isEnabled())
                                                                 .build();
    }
}
