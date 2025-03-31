package com.mycompany.useraccess.config;


import com.mycompany.useraccess.enums.AppType;
import com.mycompany.useraccess.enums.Role;
import com.mycompany.useraccess.model.ApplicationType;
import com.mycompany.useraccess.model.RoleEntity;
import com.mycompany.useraccess.model.RolePermission;
import com.mycompany.useraccess.repository.ApplicationTypeRepository;
import com.mycompany.useraccess.repository.RoleEntityRepository;
import com.mycompany.useraccess.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ApplicationTypeRepository applicationTypeRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Bean
    public CommandLineRunner initDefaultData() {
        return args -> {

            // 1. Insert ApplicationType (e.g., HOSPITAL)
            ApplicationType hospitalApp = applicationTypeRepository
                    .findByName(AppType.HOSPITAL.name())
                    .orElseGet(() -> {
                        ApplicationType app = new ApplicationType();
                        app.setName(AppType.HOSPITAL.name()); // convert enum to String
                        return applicationTypeRepository.save(app); // returns ApplicationType
                    });



            log.info("✅ ApplicationType initialized: {}", hospitalApp.getName());

            // 2. Insert RoleEntity for each Role
            Arrays.stream(Role.values()).forEach(roleEnum -> {
                roleEntityRepository.findByRoleAndAppType(roleEnum, hospitalApp)
                                    .orElseGet(() -> {
                                        RoleEntity roleEntity = RoleEntity.builder()
                                                                          .role(roleEnum)
                                                                          .appType(hospitalApp)
                                                                          .name(roleEnum.name())
                                                                          .build();
                                        roleEntityRepository.save(roleEntity);
                                        log.info("✅ RoleEntity created: {}", roleEnum);
                                        return roleEntity;
                                    });
            });


            // 3. Insert RolePermissions (e.g., SUPER_ADMIN can create all others)
            RoleEntity superAdmin = roleEntityRepository.findByRoleAndAppType(Role.SUPER_ADMIN, hospitalApp).orElseThrow();
            for (Role creatable : Arrays.asList(Role.DOCTOR, Role.NURSE, Role.RECEPTIONIST, Role.PATIENT)) {
                RoleEntity targetRole = roleEntityRepository.findByRoleAndAppType(creatable, hospitalApp).orElseThrow();

                boolean exists = rolePermissionRepository
                        .findByCreatorRoleAndCreatableRole(superAdmin, targetRole)
                        .isPresent();

                if (!exists) {
                    RolePermission permission = new RolePermission();
                    permission.setCreatorRole(superAdmin);
                    permission.setCreatableRole(targetRole);
                    permission.setCanCreate(true);
                    rolePermissionRepository.save(permission);
                    log.info("🔐 SUPER_ADMIN can create {}", creatable.name());
                }
            }

            log.info("🚀 Role & Permission setup completed.");
        };
    }
}
