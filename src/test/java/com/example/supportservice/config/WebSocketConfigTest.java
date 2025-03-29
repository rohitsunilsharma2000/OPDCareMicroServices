package com.example.supportservice.config;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebSocketConfigTest {

    @Test
    void contextLoads(ApplicationContext context) {
        // Verifies the WebSocketConfig bean is loaded successfully
        assertThat(context.getBean(WebSocketConfig.class)).isNotNull();
    }
}
