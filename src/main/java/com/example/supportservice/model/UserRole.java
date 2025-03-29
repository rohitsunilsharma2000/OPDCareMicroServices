package com.example.supportservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(exclude = "subordinates")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reports_to_id")
    private UserRole reportsTo;


}