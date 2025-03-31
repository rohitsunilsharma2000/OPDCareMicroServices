package com.mycompany.useraccess.enums;

/**
 * Enumeration of user roles across different application types.
 *
 * <p><strong>Usage:</strong> Determines access control and feature visibility for users.
 * Used in security configurations, registration logic, and permission checks.</p>
 *
 * <p><strong>Acceptable Values:</strong></p>
 * <ul>
 *     <li>{@code SUPER_ADMIN} – Highest authority; manages application-level configurations.</li>
 *     <li>{@code ADMIN} – Standard admin, lower than SUPER_ADMIN (optional for some systems).</li>
 *     <li>{@code DOCTOR} – Medical role in healthcare systems.</li>
 *     <li>{@code NURSE} – Medical support role in hospital environments.</li>
 *     <li>{@code RECEPTIONIST} – Front-desk or operational staff in healthcare systems.</li>
 *     <li>{@code STAFF} – General non-medical staff (used in other app types).</li>
 *     <li>{@code PATIENT} – End user receiving medical services or content.</li>
 * </ul>
 */
public enum Role {
    SUPER_ADMIN,
    ADMIN,
    DOCTOR,
    NURSE,
    RECEPTIONIST,
    STAFF,
    PATIENT,
    CUSTOMER
}
