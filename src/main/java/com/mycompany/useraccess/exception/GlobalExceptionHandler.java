package com.mycompany.useraccess.exception;

import com.mycompany.useraccess.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Global exception handler that catches and formats all errors across the application.
 *
 * <p><strong>Overview:</strong></p>
 * Centralized handling of all controller-level exceptions including:
 * authentication errors, validation failures, access denial, resource not found,
 * and unhandled server errors.
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *     <li>Intercepts exceptions using {@code @ExceptionHandler}.</li>
 *     <li>Returns structured and reusable {@link com.mycompany.useraccess.dto.ErrorResponseDTO} objects.</li>
 *     <li>Supports i18n error messages via {@link org.springframework.context.MessageSource}.</li>
 *     <li>Logs exceptions at appropriate severity (error, warn).</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> Exceptions are converted into well-formatted JSON responses with appropriate HTTP status.</li>
 *     <li><strong>Fail:</strong> Unhandled exceptions return a generic 500 response and are logged.</li>
 * </ul>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Message source for resolving internationalized error messages.
     *
     * <p><strong>Constraints:</strong> Injected by Spring context, used for i18n support.</p>
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Handles bad credentials during login.
     *
     * @param ex      Thrown when invalid username/password is provided.
     * @param request WebRequest context
     * @return 401 Unauthorized with translated message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return buildErrorResponse("error.login.failed", HttpStatus.UNAUTHORIZED, request, null);
    }

    /**
     * Handles login attempts for disabled or unverified accounts.
     *
     * @param ex      Thrown when account is not active.
     * @param request WebRequest context
     * @return 403 Forbidden response
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponseDTO> handleDisabledException(DisabledException ex, WebRequest request) {
        return buildErrorResponse("error.account.disabled", HttpStatus.FORBIDDEN, request, null);
    }

    /**
     * Handles unauthorized access when roles are insufficient.
     *
     * @param ex      Thrown when access is denied due to role restriction.
     * @param request WebRequest context
     * @return 403 Forbidden response
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(Exception ex, WebRequest request) {
        return buildErrorResponse("error.access.denied", HttpStatus.FORBIDDEN, request, null);
    }

    /**
     * Handles @Valid annotated DTO validation errors.
     *
     * <p>Returns all field and object-level validation failures.</p>
     *
     * @param ex      Thrown on validation failure
     * @param request WebRequest context
     * @return 400 Bad Request with field errors map
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                                                               fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                                                                fieldErrors.put(error.getObjectName(), error.getDefaultMessage())
        );

        return buildErrorResponse("error.validation", HttpStatus.BAD_REQUEST, request, fieldErrors);
    }

    /**
     * Handles not found exceptions for missing entities or records.
     *
     * @param ex      Thrown when requested resource is not found
     * @param request WebRequest context
     * @return 404 Not Found response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request, null);
    }

    /**
     * Handles 4xx client errors from external API calls.
     *
     * @param ex      Thrown by RestTemplate/WebClient on client error
     * @param request WebRequest context
     * @return 4xx response with error reason
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildErrorResponse(status.getReasonPhrase(), status, request, null);
    }

    /**
     * Handles 5xx server errors from external API calls.
     *
     * @param ex      Thrown by RestTemplate/WebClient on server error
     * @param request WebRequest context
     * @return 5xx response with error reason
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildErrorResponse(status.getReasonPhrase(), status, request, null);
    }

    /**
     * Handles duplicate email address exceptions during registration.
     *
     * @param ex      Custom EmailAlreadyExistsException
     * @param request WebRequest context
     * @return 409 Conflict response
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailExists(EmailAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request, null);
    }

    /**
     * Handles missing specialization exceptions for doctor roles.
     *
     * @param ex      Custom SpecializationRequiredException
     * @param request WebRequest context
     * @return 400 Bad Request response
     */
    @ExceptionHandler(SpecializationRequiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleSpecializationRequired(SpecializationRequiredException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, null);
    }

    /**
     * Handles runtime violations of business logic or unimplemented functionality.
     *
     * @param ex      UnsupportedOperationException
     * @param request WebRequest context
     * @return 400 Bad Request with message
     */
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnsupportedOperation(UnsupportedOperationException ex, WebRequest request) {
        log.warn("Unsupported operation: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, null);
    }

    /**
     * Catch-all handler for unexpected exceptions.
     *
     * @param ex      Generic exception
     * @param request WebRequest context
     * @return 500 Internal Server Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled exception: ", ex);
        return buildErrorResponse("error.internal", HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    /**
     * Builds the error response using i18n support and field validation info.
     *
     * @param messageKeyOrText Error message key or plain string
     * @param status           HTTP status code
     * @param request          WebRequest for path info
     * @param fieldErrors      Field validation map (nullable)
     * @return ResponseEntity with formatted error
     */
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(String messageKeyOrText,
                                                                HttpStatus status,
                                                                WebRequest request,
                                                                Map<String, String> fieldErrors) {
        String message = resolveMessage(messageKeyOrText, request.getLocale());

        ErrorResponseDTO response = ErrorResponseDTO.builder()
                                                    .timestamp(new Date())
                                                    .status(status.value())
                                                    .error(status.getReasonPhrase())
                                                    .message(message)
                                                    .path(request.getDescription(false))
                                                    .fieldErrors(fieldErrors)
                                                    .build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * Resolves message using Spring's MessageSource and locale.
     * Falls back to raw key if message not found.
     *
     * @param key    i18n key or fallback message
     * @param locale Client locale
     * @return Resolved message string
     */
    private String resolveMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
