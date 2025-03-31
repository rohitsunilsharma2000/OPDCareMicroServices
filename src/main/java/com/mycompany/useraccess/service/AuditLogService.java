package com.mycompany.useraccess.service;

import com.mycompany.useraccess.model.AuditLog;
import com.mycompany.useraccess.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public void log(String actorEmail, String action, String targetEntity, String targetId, String details) {
        AuditLog log = new AuditLog();
        log.setActorEmail(actorEmail);
        log.setAction(action);
        log.setTargetEntity(targetEntity);
        log.setTargetId(targetId);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
