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

/**
 * Configuration class responsible for seeding default data such as application types,
 * roles, and permissions into the database upon application startup.
 *
 * <p><strong>Overview:</strong></p>
 * Ensures the HOSPITAL application type exists, sets up all predefined roles under it,
 * and grants SUPER_ADMIN the ability to create specific roles.
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *     <li>Idempotently creates the "HOSPITAL" application type if it doesn't exist.</li>
 *     <li>Initializes all {@link Role} entities for the HOSPITAL context.</li>
 *     <li>Establishes creation permissions for SUPER_ADMIN to create staff and patient roles.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> All entities are created without duplication, and logs confirm setup success.</li>
 *     <li><strong>Fail:</strong> Missing enums or repository failures throw exceptions and interrupt bootstrapping.</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    /**
     * Repository for managing {@link ApplicationType} entities.
     *
     * <p><strong>Constraints:</strong> Should not be null. Injected by Spring context.</p>
     */
    private final ApplicationTypeRepository applicationTypeRepository;

    /**
     * Repository for managing {@link RoleEntity} entries.
     *
     * <p><strong>Constraints:</strong> Should not be null. Injected by Spring context.</p>
     */
    private final RoleEntityRepository roleEntityRepository;

    /**
     * Repository for managing {@link RolePermission} relationships between roles.
     *
     * <p><strong>Constraints:</strong> Should not be null. Injected by Spring context.</p>
     */
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Initializes default system-level data like application types, roles, and permission mappings.
     *
     * <p><strong>Description:</strong></p>
     * This method uses Spring's {@link CommandLineRunner} to execute once at application startup.
     * The process ensures the following:
     * <ul>
     *     <li>The application type {@code HOSPITAL} exists (created if absent).</li>
     *     <li>All roles under {@link Role} are inserted with reference to the application type.</li>
     *     <li>{@code SUPER_ADMIN} is permitted to create key user roles.</li>
     * </ul>
     *
     * <p><strong>Acceptable Values / Range:</strong></p>
     * <ul>
     *     <li>AppType: {@code HOSPITAL} is the only one initialized here, more may be added later.</li>
     *     <li>Roles: All values from the {@link Role} enum.</li>
     * </ul>
     *
     * <p><strong>Error Conditions:</strong></p>
     * <ul>
     *     <li>If a required {@link RoleEntity} is not found in the database, {@link java.util.NoSuchElementException} is thrown.</li>
     *     <li>Repository write operations may fail if database access is misconfigured.</li>
     * </ul>
     *
     * @return A CommandLineRunner to initialize data on startup.
     */
    @Bean
    public CommandLineRunner initDefaultData() {
        return args -> {

            // Step 1: Ensure HOSPITAL application type exists or create it
            ApplicationType hospitalApp = applicationTypeRepository
                    .findByName(AppType.HOSPITAL.name())
                    .orElseGet(() -> {
                        ApplicationType app = new ApplicationType();
                        app.setName(AppType.HOSPITAL.name()); // Set name from enum
                        return applicationTypeRepository.save(app); // Save and return
                    });

            log.info("ApplicationType initialized: {}", hospitalApp.getName());

            // Step 2: Ensure each role under Role enum is created for the HOSPITAL application type
            Arrays.stream(Role.values()).forEach(roleEnum -> {
                roleEntityRepository.findByRoleAndAppType(roleEnum, hospitalApp)
                                    .orElseGet(() -> {
                                        RoleEntity roleEntity = RoleEntity.builder()
                                                                          .role(roleEnum)
                                                                          .appType(hospitalApp)
                                                                          .name(roleEnum.name()) // Set name as string representation
                                                                          .build();
                                        roleEntityRepository.save(roleEntity); // Save role
                                        log.info("RoleEntity created: {}", roleEnum.name());
                                        return roleEntity;
                                    });
            });

            // Step 3: Retrieve SUPER_ADMIN role (must exist now)
            RoleEntity superAdmin = roleEntityRepository
                    .findByRoleAndAppType(Role.SUPER_ADMIN, hospitalApp)
                    .orElseThrow(); // Throws if not found

            // Step 4: Define which roles SUPER_ADMIN can create
            for (Role creatable : Arrays.asList(Role.DOCTOR, Role.NURSE, Role.RECEPTIONIST, Role.PATIENT)) {

                // Fetch target role entity
                RoleEntity targetRole = roleEntityRepository
                        .findByRoleAndAppType(creatable, hospitalApp)
                        .orElseThrow(); // Ensure target exists

                // Check if permission already exists
                boolean exists = rolePermissionRepository
                        .findByCreatorRoleAndCreatableRole(superAdmin, targetRole)
                        .isPresent();

                // If not exists, grant creation permission to SUPER_ADMIN
                if (!exists) {
                    RolePermission permission = new RolePermission();
                    permission.setCreatorRole(superAdmin);
                    permission.setCreatableRole(targetRole);
                    permission.setCanCreate(true); // Set permission flag
                    rolePermissionRepository.save(permission);
                    log.info("SUPER_ADMIN granted create permission for role: {}", creatable.name());
                }
            }

            log.info("Role and Permission setup completed successfully.");
        };
    }
}
