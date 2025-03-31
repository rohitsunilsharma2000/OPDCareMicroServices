package com.mycompany.useraccess.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Configuration class for loading i18n messages.
 */
@Configuration
public class InternationalizationConfig {

    /**
     * Registers a MessageSource bean to resolve error message keys
     * from messages.properties and locale-specific variants.
     *
     * @return the configured message source
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // loads messages.properties from resources
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true); // fallback to key if message not found
        return messageSource;
    }
}
