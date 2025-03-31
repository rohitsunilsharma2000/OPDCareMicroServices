package com.mycompany.useraccess.enums;

/**
 * Enumeration representing different types of applications supported by the system.
 *
 * <p><strong>Usage:</strong> Used in {@link com.mycompany.useraccess.dto.RegistrationRequestDTO}
 * and associated models to distinguish between tenant applications.</p>
 *
 * <p><strong>Acceptable Values:</strong></p>
 * <ul>
 *     <li>{@code HOSPITAL} – For healthcare-related features and workflows.</li>
 *     <li>{@code E_LEARNING} – For education management and online classes.</li>
 *     <li>{@code E_COMMERCE} – For product management and order workflows.</li>
 * </ul>
 */
public enum AppType {
    HOSPITAL,
    E_LEARNING,
    E_COMMERCE
}
