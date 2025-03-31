package com.mycompany.useraccess.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Setter
@Getter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actorEmail;
    private String action;
    private String targetEntity;
    private String targetId;
    private String details;
    private LocalDateTime timestamp = LocalDateTime.now();
}
