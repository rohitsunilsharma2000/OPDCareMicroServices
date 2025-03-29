package com.example.supportservice.repository;


import com.example.supportservice.enums.TicketStatus;
import com.example.supportservice.model.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatusAndUpdatedAtBefore ( TicketStatus pending , LocalDateTime minusDays );
}