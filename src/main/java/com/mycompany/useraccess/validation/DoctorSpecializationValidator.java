package com.mycompany.useraccess.validation;

import com.mycompany.useraccess.dto.RegistrationRequestDTO;
import com.mycompany.useraccess.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoctorSpecializationValidator implements ConstraintValidator<ValidDoctorSpecialization, RegistrationRequestDTO> {

    @Override
    public boolean isValid(RegistrationRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getRole() == Role.DOCTOR) {
            return dto.getSpecialization() != null && !dto.getSpecialization().trim().isEmpty();
        }
        return true; // Not a doctor, so specialization is optional
    }
}
