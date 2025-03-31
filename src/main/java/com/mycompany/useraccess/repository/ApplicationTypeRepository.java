package com.mycompany.useraccess.repository;

import com.mycompany.useraccess.model.ApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ApplicationTypeRepository extends JpaRepository<ApplicationType, Long> {


    Optional<ApplicationType> findByName(String name);  // ✅ CORRECT
}