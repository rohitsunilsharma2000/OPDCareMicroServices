package com.example.supportservice.model;


import com.example.supportservice.enums.Priority;
import com.example.supportservice.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a support ticket in the system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    /**
     * Username or email of the assigned agent.
     * Optional: Replace with @ManyToOne if you want to reference User directly.
     */
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>(); // ✅ initialize


    @ManyToOne
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;



}
