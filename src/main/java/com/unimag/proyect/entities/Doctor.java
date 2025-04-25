package com.unimag.proyect.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    private String speciality;

    @Column(nullable = false)
    private LocalTime avalibleFrom;

    @Column(nullable = false)
    private LocalTime avalibleTo;


}
