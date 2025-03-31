package com.mycompany.useraccess.model;

import com.mycompany.useraccess.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be valid")
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enabled;

    @Column(name = "approval_status")
    private String approvalStatus;

    private String specialization;

    private Long departmentId;

    private String createdBy;

    private boolean superAdmin;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ Builder for required + optional fields
    @Builder
    public User(
            @NotBlank(message = "Username is required") String username,
            @NotBlank(message = "Email is required") @Email String email,
            @NotBlank(message = "Password is required") String password,
            @NotBlank(message = "Phone number is required") String phone,
            Role role,
            boolean enabled,
            String approvalStatus,
            String specialization,
            Long departmentId,
            String createdBy,
            boolean superAdmin,
            String address
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.enabled = enabled;
        this.approvalStatus = approvalStatus;
        this.specialization = specialization;
        this.departmentId = departmentId;
        this.createdBy = createdBy;
        this.superAdmin = superAdmin;
        this.address = address;
        this.createdAt = LocalDateTime.now(); // optional: override via service
    }
}
