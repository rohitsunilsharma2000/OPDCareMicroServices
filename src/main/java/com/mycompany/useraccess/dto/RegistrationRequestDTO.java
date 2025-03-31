package com.mycompany.useraccess.dto;

import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.validation.ValidDoctorSpecialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for handling user registration requests for various roles.
 *
 * <p><strong>Roles Supported:</strong> SUPER_ADMIN, DOCTOR, NURSE, RECEPTIONIST, PATIENT</p>
 * <p><strong>Validated by:</strong> {@link ValidDoctorSpecialization}</p>
 */
@Data
@ValidDoctorSpecialization
public class RegistrationRequestDTO {

    /**
     * Unique username for the user.
     *
     * <p><strong>Constraints:</strong> Cannot be blank.</p>
     */
    @NotBlank
    private String username;

    /**
     * Email address of the user.
     *
     * <p><strong>Constraints:</strong> Must be valid and not blank.</p>
     */
    @NotBlank
    @Email
    private String email;

    /**
     * Raw password provided at the time of registration.
     *
     * <p><strong>Constraints:</strong> Cannot be blank. Should meet complexity rules (enforced externally).</p>
     */
    @NotBlank
    private String password;

    /**
     * Contact phone number of the user.
     *
     * <p><strong>Constraints:</strong> Cannot be blank.</p>
     */
    @NotBlank
    private String phone;

    /**
     * Optional address of the user.
     */
    private String address;

    /**
     * Role to be assigned to the user.
     *
     * <p><strong>Acceptable Values:</strong> One of {@link Role} enum constants.</p>
     */
    private Role role;

    /**
     * Doctor's specialization. Required only when role is DOCTOR.
     *
     * <p><strong>Constraint:</strong> Validated by {@link ValidDoctorSpecialization}.</p>
     */
    private String specialization;

    /**
     * Application type to which the user belongs.
     *
     * <p><strong>Acceptable Values:</strong> One of {@link AppType} enum constants.</p>
     */
    private AppType appType;

    /**
     * Department ID to which the user (e.g., DOCTOR or NURSE) is associated.
     *
     * <p><strong>Constraints:</strong> Optional. Required only for specific roles.</p>
     */
    private Long departmentId;
}
