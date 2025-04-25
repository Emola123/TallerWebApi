package com.unimag.proyect.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimag.proyect.dtos.MedicalRecordDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.unimag.proyect.services.MedicalRecordService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
@Import(MedicalRecordControllerTest.MockConfig.class)
class MedicalRecordControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MedicalRecordService medicalRecordService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public MedicalRecordService medicalRecordService() {
            return Mockito.mock(MedicalRecordService.class);
        }
    }

    @Test
    void shouldCreateMedicalRecord() throws Exception {
        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .id(1L)
                .appointmentId(1L)
                .patientId(1L)
                .diagnosis("Gripe")
                .notes("Reposo e hidratación")
                .createdAt(LocalDateTime.now())
                .build();

        when(medicalRecordService.createMedicalRecord(any())).thenReturn(dto);

        mockMvc.perform(post("/api/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.diagnosis").value("Gripe"));
    }

    @Test
    void shouldGetAllMedicalRecords() throws Exception {
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(
                MedicalRecordDTO.builder().id(1L).diagnosis("Gripe").patientId(1L).build()
        ));

        mockMvc.perform(get("/api/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnosis").value("Gripe"));
    }

    @Test
    void shouldGetMedicalRecordById() throws Exception {
        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .id(1L)
                .appointmentId(1L)
                .patientId(1L)
                .diagnosis("Alergia")
                .notes("Evitar el polvo")
                .createdAt(LocalDateTime.now())
                .build();

        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdateMedicalRecord() throws Exception {
        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .id(1L)
                .appointmentId(1L)
                .patientId(1L)
                .diagnosis("Asma")
                .notes("Inhalador diario")
                .createdAt(LocalDateTime.now())
                .build();

        when(medicalRecordService.updateMedicalRecord(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/records/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Asma"));
    }

    @Test
    void shouldDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/api/records/1"))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).deleteMedicalRecord(1L);
    }
}
