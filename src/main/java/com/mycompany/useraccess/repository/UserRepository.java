package com.mycompany.useraccess.repository;


import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    long countByRoleAndEnabledTrue ( Role superAdmin );

    List<User> findByApprovalStatus ( String s );
}
