package com.example.supportservice.service;

import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.repository.TicketRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketReminderScheduler {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 10 * * ?") // Every day at 10 AM
    public void sendReminders() throws MessagingException {
        List<Ticket> pendingTickets = ticketRepository.findByStatusAndUpdatedAtBefore(TicketStatus.PENDING, LocalDateTime.now().minusDays(2));
        for (Ticket ticket : pendingTickets) {
//            emailService.sendEmail(ticket.getAssignedTo(),
//                                   "Reminder: Pending Ticket #" + ticket.getId(),
//                                   "Please follow up on this ticket.");

            emailService.sendEmail(
                    ticket.getAssignedTo(),
                    "📌 Reminder: Ticket #" + ticket.getId() + " Still Open",
                    "This ticket has been open since " + ticket.getUpdatedAt() + ". Please take action."
            );
        }
    }
}
