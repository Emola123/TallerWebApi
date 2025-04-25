package com.unimag.proyect.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimag.proyect.dtos.PatientDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.unimag.proyect.services.PatientService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(PatientControllerTest.MockConfig.class)
class PatientControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PatientService patientService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public PatientService patientService() {
            return Mockito.mock(PatientService.class);
        }
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientDTO dto = PatientDTO.builder()
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan.perez@example.com")
                .phone("3001234567")
                .build();

        when(patientService.createPatient(any())).thenReturn(dto);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Juan Pérez"));
    }

    @Test
    void shouldGetAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of(
                PatientDTO.builder().id(1L).fullName("Ana María").email("ana@example.com").build()
        ));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("ana@example.com"));
    }

    @Test
    void shouldGetPatientById() throws Exception {
        PatientDTO dto = PatientDTO.builder()
                .id(1L)
                .fullName("Carlos Mendoza")
                .email("carlos@example.com")
                .phone("3112233445")
                .build();

        when(patientService.getPatientById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        PatientDTO dto = PatientDTO.builder()
                .id(1L)
                .fullName("Carlos M. Actualizado")
                .email("carlos.actualizado@example.com")
                .phone("3110000000")
                .build();

        when(patientService.updatePatient(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("carlos.actualizado@example.com"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService).deletePatient(1L);
    }
}
