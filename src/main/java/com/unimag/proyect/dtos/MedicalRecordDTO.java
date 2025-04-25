package com.unimag.proyect.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDTO {
    private Long id;

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientId;

    private String diagnosis;
    private String notes;
    private LocalDateTime createdAt;
}
