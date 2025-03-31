package com.mycompany.useraccess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Standard structure for returning error responses in API.
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDTO {

    /**
     * Time at which the error occurred.
     */
    private Date timestamp;

    /**
     * HTTP status code (e.g., 400, 404).
     */
    private int status;

    /**
     * Short name of the error (e.g., Bad Request, Forbidden).
     */
    private String error;

    /**
     * Human-readable message for the client.
     */
    private String message;

    /**
     * Path of the endpoint that caused the error.
     */
    private String path;

    /**
     * Optional field-level errors for validation exceptions.
     */
    private Map<String, String> fieldErrors;
}
