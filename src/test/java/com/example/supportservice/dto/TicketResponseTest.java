package com.example.supportservice.dto;

import com.example.supportservice.enums.Priority;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Attachment;
import com.example.supportservice.model.Ticket;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketResponseTest {

    @Test
    void testFromEntityMapsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        Attachment attachment = Attachment.builder()
                                          .fileUrl("http://localhost/file1.pdf")
                                          .build();

        Ticket ticket = Ticket.builder()
                              .id(1L)
                              .subject("Issue")
                              .description("Details")
                              .priority(Priority.MEDIUM)
                              .status(TicketStatus.OPEN)
                              .assignedTo("agent1@example.com")
                              .createdAt(now)
                              .updatedAt(now)
                              .attachments(Collections.singletonList(attachment))
                              .build();

        TicketResponse response = TicketResponse.from(ticket);

        assertThat(response.getId()).isEqualTo(ticket.getId());
        assertThat(response.getSubject()).isEqualTo(ticket.getSubject());
        assertThat(response.getDescription()).isEqualTo(ticket.getDescription());
        assertThat(response.getPriority()).isEqualTo(ticket.getPriority());
        assertThat(response.getStatus()).isEqualTo(ticket.getStatus());
        assertThat(response.getAssignedTo()).isEqualTo(ticket.getAssignedTo());
        assertThat(response.getCreatedAt()).isEqualTo(ticket.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(ticket.getUpdatedAt());
        assertThat(response.getAttachmentUrls()).containsExactly("http://localhost/file1.pdf");
    }

    @Test
    void testFromEntityWithNullAttachments() {
        Ticket ticket = Ticket.builder()
                              .id(2L)
                              .subject("No attachments")
                              .description("Description")
                              .priority(Priority.LOW)
                              .status(TicketStatus.PENDING)
                              .assignedTo(null)
                              .attachments(null)
                              .build();

        TicketResponse response = TicketResponse.from(ticket);
        assertThat(response.getAttachmentUrls()).isNull();
    }
}
