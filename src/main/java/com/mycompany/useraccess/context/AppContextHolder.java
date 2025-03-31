package com.mycompany.useraccess.context;

import com.mycompany.useraccess.enums.AppType;

/**
 * Thread-safe context holder for storing and retrieving the current {@link AppType}
 * for a given request thread.
 *
 * <p><strong>Overview:</strong></p>
 * This class uses {@link ThreadLocal} to associate an {@link AppType} with the current thread.
 * It enables request-scoped application logic to be aware of the active application context
 * (e.g., HOSPITAL, CLINIC).
 *
 * <p><strong>Use Cases:</strong></p>
 * <ul>
 *     <li>Multi-tenant applications where logic varies based on the application type.</li>
 *     <li>Authorization or filtering mechanisms that depend on the calling context.</li>
 *     <li>Auditing or logging application-specific actions.</li>
 * </ul>
 *
 * <p><strong>Thread Safety:</strong></p>
 * <ul>
 *     <li>This class is thread-safe by design due to the use of {@link ThreadLocal}.</li>
 *     <li>Values must be cleared after request completion to prevent memory leaks.</li>
 * </ul>
 */
public class AppContextHolder {

    /**
     * Thread-local storage for holding the current {@link AppType} for each request.
     *
     * <p><strong>Constraints:</strong> Should always be cleared using {@link #clear()} in request filters or interceptors.</p>
     */
    private static final ThreadLocal<AppType> appTypeHolder = new ThreadLocal<>();

    /**
     * Sets the {@link AppType} for the current thread context.
     *
     * <p><strong>Description:</strong></p>
     * Associates a specific application type (e.g., HOSPITAL) with the current thread, making it
     * available throughout the execution of that request.
     *
     * @param appType The application type to set.
     *                <ul>
     *                    <li><strong>Acceptable Values:</strong> Must be non-null. Enum defined in {@link AppType}.</li>
     *                </ul>
     * @throws IllegalArgumentException if {@code appType} is null.
     */
    public static void setAppType(AppType appType) {
        if (appType == null) {
            throw new IllegalArgumentException("AppType cannot be null");
        }
        appTypeHolder.set(appType);
    }

    /**
     * Retrieves the {@link AppType} associated with the current thread.
     *
     * @return The application type, or {@code null} if not set.
     *
     * <p><strong>Usage Warning:</strong> Always check for null to avoid {@code NullPointerException}.</p>
     */
    public static AppType getAppType() {
        return appTypeHolder.get();
    }

    /**
     * Clears the {@link AppType} associated with the current thread.
     *
     * <p><strong>Description:</strong></p>
     * This should be called at the end of request processing to avoid memory leaks
     * or stale context in thread pools (especially in async setups).
     */
    public static void clear() {
        appTypeHolder.remove();
    }
}
