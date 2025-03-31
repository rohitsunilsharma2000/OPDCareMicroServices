package com.mycompany.useraccess.repository;

import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.model.RoleEntity;
import com.mycompany.useraccess.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {



    List<RolePermission> findByCreatorRoleRoleAndCanCreateTrue(String creatorRoleName);

    boolean existsByCreatorRoleRoleAndCreatableRoleRole ( Role role , Role creatable );

    Optional<Object> findByCreatorRoleAndCreatableRole ( RoleEntity superAdmin , RoleEntity targetRole );
}
