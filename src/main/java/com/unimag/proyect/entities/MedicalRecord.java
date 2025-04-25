package com.unimag.proyect.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Appointment appointment;

    @ManyToOne(optional = false)
    private Patient patient;

    @Column(nullable = false)
    private String diagnosis;

    @Column(nullable = false)
    private String notes;

    private LocalDateTime createdAt;
}
