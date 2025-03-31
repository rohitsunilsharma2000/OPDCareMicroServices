package com.mycompany.useraccess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleDashboardController {

    @GetMapping("/doctor/dashboard")
    public ResponseEntity<String> doctorDashboard() {
        return ResponseEntity.ok("Welcome to the Doctor Dashboard");
    }

    @GetMapping("/nurse/dashboard")
    public ResponseEntity<String> nurseDashboard() {
        return ResponseEntity.ok("Welcome to the Nurse Dashboard");
    }

    @GetMapping("/receptionist/dashboard")
    public ResponseEntity<String> receptionistDashboard() {
        return ResponseEntity.ok("Welcome to the Receptionist Dashboard");
    }

    @GetMapping("/patient/dashboard")
    public ResponseEntity<String> patientDashboard() {
        return ResponseEntity.ok("Welcome to the Patient Dashboard");
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Welcome to the Super Admin Dashboard");
    }
}
