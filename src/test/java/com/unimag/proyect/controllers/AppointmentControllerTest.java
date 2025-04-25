package com.unimag.proyect.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimag.proyect.dtos.AppointmentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.unimag.proyect.services.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import(AppointmentControllerTest.MockConfig.class)
class AppointmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AppointmentService appointmentService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public AppointmentService appointmentService() {
            return Mockito.mock(AppointmentService.class);
        }
    }

    @Test
    void shouldCreateAppointment() throws Exception {
        AppointmentDTO dto = AppointmentDTO.builder()
                .id(1L)
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status("SCHEDULED")
                .build();

        when(appointmentService.createAppointment(any())).thenReturn(dto);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void shouldGetAllAppointments() throws Exception {
        AppointmentDTO dto = AppointmentDTO.builder()
                .id(1L)
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .status("SCHEDULED")
                .build();

        when(appointmentService.getAppointments()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L));
    }

    @Test
    void shouldGetAppointmentById() throws Exception {
        AppointmentDTO dto = AppointmentDTO.builder()
                .id(1L)
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .status("SCHEDULED")
                .build();

        when(appointmentService.getAppointmentId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdateAppointment() throws Exception {
        AppointmentDTO dto = AppointmentDTO.builder()
                .id(1L)
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(2))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(1))
                .status("SCHEDULED")
                .build();

        when(appointmentService.updateAppointment(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void shouldDeleteAppointment() throws Exception {
        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());

        verify(appointmentService).deleteAppointment(1L);
    }
}
