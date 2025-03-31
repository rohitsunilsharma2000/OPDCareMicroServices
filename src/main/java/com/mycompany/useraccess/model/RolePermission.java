package com.mycompany.useraccess.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_role_id", nullable = false)
    private RoleEntity creatorRole;

    @ManyToOne
    @JoinColumn(name = "creatable_role_id", nullable = false)
    private RoleEntity creatableRole;

    @Column(name = "can_create")
    private boolean canCreate;

}
