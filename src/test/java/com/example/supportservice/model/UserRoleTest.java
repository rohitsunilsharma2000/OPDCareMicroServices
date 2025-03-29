package com.example.supportservice.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleTest {

    @Test
    void testUserRoleBuilder() {
        UserRole manager = UserRole.builder()
                                   .id(1L)
                                   .name("Manager")
                                   .description("Manages team")
                                   .build();

        UserRole agent = UserRole.builder()
                                 .id(2L)
                                 .name("Agent")
                                 .description("Handles tickets")
                                 .reportsTo(manager)
                                 .build();

        assertThat(agent.getId()).isEqualTo(2L);
        assertThat(agent.getName()).isEqualTo("Agent");
        assertThat(agent.getReportsTo()).isEqualTo(manager);
    }

    @Test
    void testHierarchyToStringAvoidsSubordinates() {
        UserRole role = new UserRole();
        String result = role.toString();
        assertThat(result).doesNotContain("subordinates");
    }
}
