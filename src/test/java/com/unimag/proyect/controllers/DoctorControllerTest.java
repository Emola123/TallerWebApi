package com.unimag.proyect.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimag.proyect.dtos.DoctorDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.unimag.proyect.services.DoctorService;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@Import(DoctorControllerTest.MockConfig.class)
class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DoctorService doctorService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public DoctorService doctorService() {
            return Mockito.mock(DoctorService.class);
        }
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        DoctorDTO dto = DoctorDTO.builder()
                .id(1L)
                .fullName("Dr. Strange")
                .email("strange@hospital.com")
                .speciality("Neurocirugía")
                .avalibleFrom(LocalTime.of(8, 0))
                .avalibleTo(LocalTime.of(16, 0))
                .build();

        when(doctorService.createDoctor(any())).thenReturn(dto);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Dr. Strange"));
    }

    @Test
    void shouldGetAllDoctors() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(List.of(
                DoctorDTO.builder().id(1L).fullName("Dr. House").speciality("Diagnóstico").build()
        ));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].speciality").value("Diagnóstico"));
    }

    @Test
    void shouldGetDoctorById() throws Exception {
        DoctorDTO dto = DoctorDTO.builder()
                .id(1L)
                .fullName("Dr. House")
                .email("house@hospital.com")
                .speciality("Diagnóstico")
                .build();

        when(doctorService.getDoctorById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdateDoctor() throws Exception {
        DoctorDTO dto = DoctorDTO.builder()
                .id(1L)
                .fullName("Dr. Wilson")
                .speciality("Oncología")
                .email("wilson@hospital.com")
                .avalibleFrom(LocalTime.of(8, 0))
                .avalibleTo(LocalTime.of(16, 0))
                .build();

        when(doctorService.updateDoctor(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speciality").value("Oncología"));
    }

    @Test
    void shouldDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteDoctor(1L);
    }
}
