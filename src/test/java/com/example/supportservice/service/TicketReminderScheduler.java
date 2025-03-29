package com.example.supportservice.service;

import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.repository.TicketRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class TicketReminderSchedulerTest {

    @Test
    void testSendReminders_shouldSendEmailForPendingTickets() throws MessagingException {
        TicketRepository ticketRepository = mock(TicketRepository.class);
        EmailService emailService = mock(EmailService.class);

        Ticket ticket = Ticket.builder()
                              .id(1L)
                              .status(TicketStatus.PENDING)
                              .updatedAt(LocalDateTime.now().minusDays(3))
                              .assignedTo("agent@example.com")
                              .build();

        when(ticketRepository.findByStatusAndUpdatedAtBefore(eq(TicketStatus.PENDING), any()))
                .thenReturn(List.of(ticket));

        TicketReminderScheduler scheduler = new TicketReminderScheduler(ticketRepository, emailService);
        scheduler.sendReminders();

        verify(emailService, times(1)).sendEmail(eq("agent@example.com"), anyString(), anyString());
    }
}
