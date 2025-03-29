package com.example.supportservice.service;

import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.dto.TicketResponse;
import com.example.supportservice.enums.Role;
import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.exception.ResourceNotFoundException;
import com.example.supportservice.exception.TicketNotFoundException;
import com.example.supportservice.model.Attachment;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.model.User;
import com.example.supportservice.repository.TicketRepository;
import com.example.supportservice.repository.UserRepository;
import com.example.supportservice.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FileStorageUtil fileStorageUtil;
    private final UserRepository userRepository;
    private final EmailService emailService; // <-- Injected for follow-up notifications

    /**
     * Create a new ticket.
     * @param request TicketRequest
     * @return TicketResponse
     */
    public TicketResponse createTicket(TicketRequest request) {
        log.info("Creating new ticket with subject: {}", request.getSubject());

        Ticket ticket = Ticket.builder()
                              .subject(request.getSubject())
                              .description(request.getDescription())
                              .priority(request.getPriority())
                              .status(TicketStatus.OPEN)
                              .createdAt(LocalDateTime.now())
                              .updatedAt(LocalDateTime.now())
                              .build();

        ticketRepository.save(ticket);

        // Send notification (optional)
        emailService.sendTicketCreatedNotification(ticket);

        return TicketResponse.from(ticket);
    }

    /**
     * Fetch a ticket by its ID.
     */
    public TicketResponse getTicketById(Long id) {
        log.info("Fetching ticket with ID: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                                        .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + id));
        return TicketResponse.from(ticket);
    }

    /**
     * Upload and attach a file to a ticket.
     */
    public void saveAttachment(Long ticketId, MultipartFile file) {
        log.info("Uploading attachment for ticket ID: {}", ticketId);

        Ticket ticket = ticketRepository.findById(ticketId)
                                        .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID " + ticketId));

        String fileUrl = fileStorageUtil.save(file);

        Attachment attachment = Attachment.builder()
                                          .fileName(file.getOriginalFilename())
                                          .fileType(file.getContentType())
                                          .fileUrl(fileUrl)
                                          .ticket(ticket)
                                          .build();

        ticket.getAttachments().add(attachment);
        ticketRepository.save(ticket);
        log.info("Attachment uploaded successfully.");
    }

    /**
     * Update ticket fields.
     */
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                                        .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID " + id));

        log.info("Updating ticket ID: {}", id);

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setAssignedTo(request.getAssignedTo());
        ticket.setStatus(request.getStatus());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketRepository.save(ticket);

        // Send notification if status changed
        if (!oldStatus.equals(ticket.getStatus())) {
            emailService.sendTicketStatusChangedNotification(ticket, oldStatus);
        }

        return TicketResponse.from(ticket);
    }

    /**
     * Retrieve all tickets (sorted by creation date desc).
     */
    public List<Ticket> getAllTickets() {
        log.info("Fetching all tickets");
        return ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Manually assign ticket to a specific agent.
     */
    public void assignTicketToAgent(Long ticketId, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User agent = userRepository.findById(agentId)
                                   .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));

        if (agent.getRole() != Role.AGENT) {
            throw new IllegalArgumentException("User is not an agent");
        }

        log.info("Assigning ticket ID {} to agent ID {}", ticketId, agentId);
        ticket.setAssignedAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);

        emailService.sendTicketAssignedNotification(ticket, agent);
    }

    /**
     * Auto-assign ticket to a random available agent.
     */
    public Long autoAssignAgent(Long ticketId) {
        List<User> agents = userRepository.findByRole(Role.AGENT);

        if (agents.isEmpty()) {
            log.warn("No agents available for auto-assignment");
            throw new RuntimeException("No agents available for assignment");
        }

        User chosen = agents.get(new Random().nextInt(agents.size()));

        log.info("Auto-assigning ticket {} to agent {}", ticketId, chosen.getEmail());
        assignTicketToAgent(ticketId, chosen.getId());

        return chosen.getId();
    }
}
