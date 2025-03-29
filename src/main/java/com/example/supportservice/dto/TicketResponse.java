package com.example.supportservice.dto;


import com.example.supportservice.enums.Priority;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Attachment;
import com.example.supportservice.model.Ticket;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for sending ticket data to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {

    private Long id;
    private String subject;
    private String description;
    private Priority priority;
    private TicketStatus status;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> attachmentUrls;

    public static TicketResponse from( Ticket ticket) {
        return TicketResponse.builder()
                             .id(ticket.getId())
                             .subject(ticket.getSubject())
                             .description(ticket.getDescription())
                             .priority(ticket.getPriority())
                             .status(ticket.getStatus())
                             .assignedTo(ticket.getAssignedTo())
                             .createdAt(ticket.getCreatedAt())
                             .updatedAt(ticket.getUpdatedAt())
                             .attachmentUrls(
                                     ticket.getAttachments() != null ?
                                             ticket.getAttachments().stream()
                                                   .map(Attachment::getFileUrl)
                                                   .collect(Collectors.toList()) : null
                             )
                             .build();
    }
}
