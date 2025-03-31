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

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final List<RegistrationStrategy> strategies;
    private final UserRepository userRepository;

    public RegistrationResponseDTO register( RegistrationRequestDTO dto) throws AccessDeniedException {
        AppType appType = dto.getAppType();
        Role role = dto.getRole();

        AppContextHolder.setAppType(appType);
        try {
            RegistrationStrategy strategy = strategies.stream()
                                                      .filter(s -> s.supports(appType, role))
                                                      .findFirst()
                                                      .orElseThrow(() -> new UnsupportedOperationException("No strategy found for role " + role + " in app " + appType));

            return strategy.register(dto);
        } catch (AccessDeniedException e) {
//            throw new RuntimeException(e);
            throw e; // ✅ Let Spring Security handle this properly as 403
        } finally {
            AppContextHolder.clear();
        }
    }

    public long count(){
        return userRepository.count();
    }
}
