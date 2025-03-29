package com.example.supportservice.service;

import com.example.supportservice.model.Ticket;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending email notifications.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends an email when a ticket's status is updated.
     *
     * @param ticket    the updated ticket
     * @param oldStatus the previous status of the ticket
     */
    public void sendTicketStatusChangedNotification(Ticket ticket, TicketStatus oldStatus) {
        try {
            String to = ticket.getAssignedTo(); // assuming assignedTo is email
            String subject = "Ticket Status Updated: #" + ticket.getId();
            String body = buildStatusUpdateEmail(ticket, oldStatus);

            sendEmail(to, subject, body);
            log.info("Status change email sent for ticket ID: {}", ticket.getId());
        } catch (Exception ex) {
            log.error("Failed to send status change email for ticket ID {}: {}", ticket.getId(), ex.getMessage());
        }
    }

    void sendEmail ( String to , String subject , String body ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true = HTML
        mailSender.send(message);
    }

    private String buildStatusUpdateEmail(Ticket ticket, TicketStatus oldStatus) {
        return String.format(
                "<p>Dear Agent,</p>" +
                        "<p>The status of ticket <strong>#%d</strong> has changed from <strong>%s</strong> to <strong>%s</strong>.</p>" +
                        "<p><strong>Subject:</strong> %s</p>" +
                        "<p><strong>Description:</strong> %s</p>" +
                        "<p>Updated at: %s</p>" +
                        "<br/><p>Regards,<br/>Support Team</p>",
                ticket.getId(),
                oldStatus,
                ticket.getStatus(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getUpdatedAt()
        );
    }

    /**
     * Sends an email to the agent when a ticket is manually or auto-assigned to them.
     *
     * @param ticket the ticket that was assigned
     * @param agent  the agent to whom the ticket was assigned
     */
    public void sendTicketAssignedNotification(Ticket ticket, User agent) {
        try {
            String to = agent.getEmail(); // assuming agent has email field
            String subject = "New Ticket Assigned: #" + ticket.getId();
            String body = String.format(
                    "<p>Dear %s,</p>" +
                            "<p>A new ticket has been assigned to you.</p>" +
                            "<p><strong>Subject:</strong> %s</p>" +
                            "<p><strong>Description:</strong> %s</p>" +
                            "<p><strong>Status:</strong> %s</p>" +
                            "<p><strong>Priority:</strong> %s</p>" +
                            "<p>Created at: %s</p>" +
                            "<br/><p>Regards,<br/>Support System</p>",
                    agent.getUsername(),
                    ticket.getSubject(),
                    ticket.getDescription(),
                    ticket.getStatus(),
                    ticket.getPriority(),
                    ticket.getCreatedAt()
            );

            sendEmail(to, subject, body);
            log.info("Ticket assigned notification sent to agent {} for ticket {}", agent.getEmail(), ticket.getId());
        } catch (Exception e) {
            log.error("Failed to send ticket assigned notification for ticket {}: {}", ticket.getId(), e.getMessage());
        }
    }

    /**
     * Sends an email to the support team or default support email when a new ticket is created.
     *
     * @param ticket the newly created ticket
     */
    public void sendTicketCreatedNotification(Ticket ticket) {
        try {
            String to = "support-team@example.com"; // Replace with actual support email or notify admin
            String subject = "New Ticket Created: #" + ticket.getId();
            String body = String.format(
                    "<p>Hello Support Team,</p>" +
                            "<p>A new ticket has been submitted.</p>" +
                            "<p><strong>Subject:</strong> %s</p>" +
                            "<p><strong>Description:</strong> %s</p>" +
                            "<p><strong>Status:</strong> %s</p>" +
                            "<p><strong>Priority:</strong> %s</p>" +
                            "<p>Created at: %s</p>" +
                            "<br/><p>Regards,<br/>Support Portal</p>",
                    ticket.getSubject(),
                    ticket.getDescription(),
                    ticket.getStatus(),
                    ticket.getPriority(),
                    ticket.getCreatedAt()
            );

            sendEmail(to, subject, body);
            log.info("Ticket created notification sent for ticket {}", ticket.getId());
        } catch (Exception e) {
            log.error("Failed to send ticket created notification for ticket {}: {}", ticket.getId(), e.getMessage());
        }
    }

}
