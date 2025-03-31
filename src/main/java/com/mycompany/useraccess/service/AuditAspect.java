package com.mycompany.useraccess.service;

import com.mycompany.useraccess.annotation.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;

    @AfterReturning("@annotation(LoggableAction) && args(.., @RequestBody body)")
    public void logAction( JoinPoint joinPoint, Object body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actorEmail = auth != null ? auth.getName() : "UNKNOWN";

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LoggableAction annotation = signature.getMethod().getAnnotation(LoggableAction.class);

        auditLogService.log(
                actorEmail,
                annotation.action(),
                annotation.targetEntity(),
                annotation.targetId(),
                body.toString()
        );
    }
}
