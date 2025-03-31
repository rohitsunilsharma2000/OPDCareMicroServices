package com.mycompany.useraccess.dto;

import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.validation.ValidDoctorSpecialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@ValidDoctorSpecialization
public class RegistrationRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    private String address;

    private Role role ;

    private String specialization;


    private AppType appType;

    private Long departmentId;
}
