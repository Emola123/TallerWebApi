package com.unimag.proyect.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;
    private String fullName;

    @Email
    @NotBlank
    private String email;

    private String speciality;
    private LocalTime avalibleFrom;
    private LocalTime avalibleTo;
}
