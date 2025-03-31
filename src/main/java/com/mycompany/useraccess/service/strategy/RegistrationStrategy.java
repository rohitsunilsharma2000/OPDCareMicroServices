package com.mycompany.useraccess.service.strategy;


import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.dto.RegistrationResponseDTO;
import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;

import java.nio.file.AccessDeniedException;


public interface RegistrationStrategy {
    boolean supports ( AppType appType , Role role );

    RegistrationResponseDTO register ( RegistrationRequestDTO dto ) throws AccessDeniedException;
}
