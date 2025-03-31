package com.mycompany.useraccess.exception;




import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;

@Slf4j
@ControllerAdvice

public class GlobalExceptionHandler {

    /**
     * Handle BadCredentialsException (wrong username/email or password)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return buildErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handle DisabledException (account is not activated)
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabledException(DisabledException ex, WebRequest request) {
        return buildErrorResponse("Account is disabled. Please verify your email before login.", HttpStatus.FORBIDDEN, request);
    }

    /**
     * Handle AccessDeniedException (unauthorized access to protected resources)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(Exception ex, WebRequest request) {
        return buildErrorResponse("Access Denied: " + ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    /**
     * Handle validation failures from @Valid DTOs
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Validation Failed");
        errorDetails.put("message", "Invalid input");
        errorDetails.put("path", request.getDescription(false));

        // Extract field errors
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                                                               fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        // 👇 Add this to handle class-level (object-level) errors like @ValidDoctorSpecialization
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            String objectName = error.getObjectName();
            fieldErrors.put(objectName, error.getDefaultMessage());
        });

        errorDetails.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle custom not found exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handle generic 4xx errors
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Handle generic 5xx errors
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Default fallback
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled exception: ", ex);
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Helper to build a consistent error response
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExists(EmailAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request); // 409
    }

    @ExceptionHandler(SpecializationRequiredException.class)
    public ResponseEntity<Map<String, Object>> handleSpecializationRequired(SpecializationRequiredException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request); // 400
    }


    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedOperation
            (UnsupportedOperationException ex, WebRequest request) {
        log.warn("🚫 Unsupported operation: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request); // 400

    }
}
