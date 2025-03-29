package com.example.supportservice.controller;


import com.example.supportservice.dto.TicketRequest;
import com.example.supportservice.dto.TicketResponse;
import com.example.supportservice.model.Ticket;
import com.example.supportservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    /**
     * Endpoint to create a new support ticket.
     * @param request TicketRequest DTO
     * @return TicketResponse DTO
     */
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket( @RequestBody @Valid TicketRequest request) {
        log.info("Received request to create a ticket.");
        return new ResponseEntity<>(ticketService.createTicket(request), HttpStatus.CREATED);
    }

    /**
     * Endpoint to fetch a ticket by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PostMapping("/{ticketId}/attachments")
    public ResponseEntity<String> uploadAttachment(
            @PathVariable Long ticketId,
            @RequestParam("file") MultipartFile file) {
        ticketService.saveAttachment(ticketId, file);
        return ResponseEntity.ok("Attachment uploaded successfully.");
    }
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }
    @GetMapping
    public ResponseEntity<List<TicketResponse>> listAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketResponse> responses = tickets.stream()
                                                .map(TicketResponse::from)
                                                .toList();

        return ResponseEntity.ok(responses);
    }
    @PostMapping("/{ticketId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignTicketToAgent(@PathVariable Long ticketId,
                                                 @RequestParam Long agentId) {
        ticketService.assignTicketToAgent(ticketId, agentId);
        return ResponseEntity.ok("Ticket assigned to agent ID: " + agentId);
    }

    @PostMapping("/{ticketId}/auto-assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> autoAssignTicket(@PathVariable Long ticketId) {
        Long assignedAgentId = ticketService.autoAssignAgent(ticketId);
        return ResponseEntity.ok("Ticket auto-assigned to agent ID: " + assignedAgentId);
    }


    // other endpoints...
}
