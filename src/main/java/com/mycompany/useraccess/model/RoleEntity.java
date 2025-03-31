package com.mycompany.useraccess.model;


import com.mycompany.useraccess.enums.Role;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "app_type_id")
    private ApplicationType appType;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Role role;

}
