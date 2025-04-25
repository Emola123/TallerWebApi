package com.unimag.proyect.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimag.proyect.dtos.ConsultRoomDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.unimag.proyect.services.ConsultRoomService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultRoomController.class)
@Import(ConsultRoomControllerTest.MockConfig.class)
class ConsultRoomControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ConsultRoomService consultRoomService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ConsultRoomService consultRoomService() {
            return Mockito.mock(ConsultRoomService.class);
        }
    }

    @Test
    void shouldCreateRoom() throws Exception {
        ConsultRoomDTO dto = ConsultRoomDTO.builder()
                .id(1L)
                .name("Room A")
                .floor(2)
                .description("Sala de cardiología")
                .build();

        when(consultRoomService.createConsultRoom(any())).thenReturn(dto);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Room A"));
    }

    @Test
    void shouldGetAllRooms() throws Exception {
        when(consultRoomService.getAllConsultRooms()).thenReturn(List.of(
                ConsultRoomDTO.builder().id(1L).name("Room A").floor(2).description("Desc").build()
        ));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Room A"));
    }

    @Test
    void shouldGetRoomById() throws Exception {
        ConsultRoomDTO dto = ConsultRoomDTO.builder()
                .id(1L)
                .name("Room B")
                .floor(1)
                .description("Sala de espera")
                .build();

        when(consultRoomService.getConsultRoomById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdateRoom() throws Exception {
        ConsultRoomDTO dto = ConsultRoomDTO.builder()
                .id(1L)
                .name("Room Updated")
                .floor(3)
                .description("Nueva descripción")
                .build();

        when(consultRoomService.updateConsultRoom(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Room Updated"));
    }

    @Test
    void shouldDeleteRoom() throws Exception {
        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isNoContent());

        verify(consultRoomService).deleteConsultRoom(1L);
    }
}
