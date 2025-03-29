package com.example.supportservice.dto;


import com.example.supportservice.enums.Priority;
import com.example.supportservice.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for creating or updating a support ticket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequest {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority must be specified")
    private Priority priority;

    private String assignedTo;

    private TicketStatus status;

}
