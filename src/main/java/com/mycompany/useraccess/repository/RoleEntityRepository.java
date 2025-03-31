package com.mycompany.useraccess.repository;


import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.model.ApplicationType;
import com.mycompany.useraccess.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Find a role entity by role and application type.
     *
     * @param role       the role enum
     * @param appType    the application type
     * @return optional RoleEntity
     */
    Optional<RoleEntity> findByRoleAndAppType(Role role, ApplicationType appType);

    /**
     * Find a role entity by name and application type.
     *
     * @param name       the role name
     * @param appType    the application type
     * @return optional RoleEntity
     */
    Optional<RoleEntity> findByNameAndAppType(String name, ApplicationType appType);
}
