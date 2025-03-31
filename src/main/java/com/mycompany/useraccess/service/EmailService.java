package com.mycompany.useraccess.service;

import com.mycompany.useraccess.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    public void sendVerificationEmail( User user) {
        // Simulate email sending
        log.info("Sending verification link to {}", user.getEmail());
    }

    public void notifyAdminsOfPendingApproval ( User doctor ) {
        log.info("Sending approval link to {}", doctor.getEmail());
    }
}
