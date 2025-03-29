package com.example.supportservice.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AttachmentTest {

    @Test
    void testAttachmentBuilderAndGetters() {
        Ticket ticket = Ticket.builder().id(1L).build();

        Attachment attachment = Attachment.builder()
                                          .id(101L)
                                          .fileName("screenshot.png")
                                          .fileType("image/png")
                                          .fileUrl("/uploads/screenshot.png")
                                          .ticket(ticket)
                                          .build();

        assertThat(attachment.getId()).isEqualTo(101L);
        assertThat(attachment.getFileName()).isEqualTo("screenshot.png");
        assertThat(attachment.getFileType()).isEqualTo("image/png");
        assertThat(attachment.getFileUrl()).isEqualTo("/uploads/screenshot.png");
        assertThat(attachment.getTicket()).isEqualTo(ticket);
    }

    @Test
    void testAttachmentToStringDoesNotIncludeTicket() {
        Attachment attachment = new Attachment();
        String result = attachment.toString();
        // This test just ensures no exception and doesn't eagerly fetch `ticket`
        assertThat(result).doesNotContain("ticket=");
    }
}
