package com.example.supportservice.exception;




import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {



    // 4xx errors (e.g., 400, 401, 403, 404, 405, 406, etc.)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException ( HttpClientErrorException ex , WebRequest request ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildErrorResponse(status.getReasonPhrase() , status , request);
    }

    // 5xx errors (e.g., 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpServerErrorException ( HttpServerErrorException ex , WebRequest request ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildErrorResponse(status.getReasonPhrase() , status , request);
    }


    // Helper method to build the error response
    private ResponseEntity<Map<String, Object>> buildErrorResponse ( String message , HttpStatus status , WebRequest request ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp" , new Date());
        errorDetails.put("message" , message);
        errorDetails.put("status" , status.value());
        errorDetails.put("error" , status.getReasonPhrase());
        errorDetails.put("path" , request.getDescription(false)); // e.g., "uri=/api/..."
        return new ResponseEntity<>(errorDetails , status);
    }


    // ✅ Security - Access Denied
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException ( Exception ex , WebRequest request ) {
        return buildErrorResponse("Access Denied: " + ex.getMessage() , HttpStatus.FORBIDDEN , request);
    }

    // ✅ ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDoctorAlreadyExistsException ( ResourceNotFoundException ex , WebRequest request ) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage() , HttpStatus.NOT_FOUND , request);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleTicketNotFound(TicketNotFoundException ex) {
        log.warn("Ticket not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}