package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.ConsultRoomDTO;
import com.unimag.proyect.entities.ConsultRoom;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import com.unimag.proyect.mappers.ConsultRoomMapper;
import com.unimag.proyect.repositories.ConsultRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultRoomServiceTest {

    @Mock
    private ConsultRoomRepository consultRoomRepository;
    @Mock
    private ConsultRoomMapper consultRoomMapper;

    @InjectMocks
    private ConsultRoomServiceImpl consultRoomService;

    @Test
    void shouldCreateConsultRoom() {
        ConsultRoomDTO dto = new ConsultRoomDTO(null, "Sala A", 2, "Piso alto");
        ConsultRoom entity = new ConsultRoom(null, "Sala A", 2, "Piso alto");
        ConsultRoom saved = new ConsultRoom(1L, "Sala A", 2, "Piso alto");
        ConsultRoomDTO savedDto = new ConsultRoomDTO(1L, "Sala A", 2, "Piso alto");

        when(consultRoomMapper.toEntity(dto)).thenReturn(entity);
        when(consultRoomRepository.save(entity)).thenReturn(saved);
        when(consultRoomMapper.toDTO(saved)).thenReturn(savedDto);

        ConsultRoomDTO result = consultRoomService.createConsultRoom(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void shouldReturnAllConsultRooms() {
        List<ConsultRoom> rooms = List.of(
                new ConsultRoom(1L, "Sala 1", 1, "Primer piso"),
                new ConsultRoom(2L, "Sala 2", 2, "Segundo piso")
        );

        List<ConsultRoomDTO> dtos = List.of(
                new ConsultRoomDTO(1L, "Sala 1", 1, "Primer piso"),
                new ConsultRoomDTO(2L, "Sala 2", 2, "Segundo piso")
        );

        when(consultRoomRepository.findAll()).thenReturn(rooms);
        when(consultRoomMapper.toDTO(rooms.get(0))).thenReturn(dtos.get(0));
        when(consultRoomMapper.toDTO(rooms.get(1))).thenReturn(dtos.get(1));

        List<ConsultRoomDTO> result = consultRoomService.getAllConsultRooms();

        assertEquals(dtos, result);
    }

    @Test
    void shouldReturnConsultRoomById() {
        ConsultRoom room = new ConsultRoom(1L, "Sala X", 3, "Tercer piso");
        ConsultRoomDTO dto = new ConsultRoomDTO(1L, "Sala X", 3, "Tercer piso");

        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(consultRoomMapper.toDTO(room)).thenReturn(dto);

        ConsultRoomDTO result = consultRoomService.getConsultRoomById(1L);

        assertEquals(dto, result);
    }

    @Test
    void shouldThrowWhenConsultRoomNotFoundById() {
        when(consultRoomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> consultRoomService.getConsultRoomById(99L));
    }

    @Test
    void shouldUpdateConsultRoom() {
        ConsultRoom existing = new ConsultRoom(1L, "Old", 1, "Old Desc");
        ConsultRoomDTO update = new ConsultRoomDTO(null, "New", 2, "New Desc");
        ConsultRoom updated = new ConsultRoom(1L, "New", 2, "New Desc");
        ConsultRoomDTO updatedDto = new ConsultRoomDTO(1L, "New", 2, "New Desc");

        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(consultRoomRepository.save(existing)).thenReturn(updated);
        when(consultRoomMapper.toDTO(updated)).thenReturn(updatedDto);

        ConsultRoomDTO result = consultRoomService.updateConsultRoom(1L, update);

        assertEquals(updatedDto, result);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentConsultRoom() {
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.empty());
        ConsultRoomDTO update = new ConsultRoomDTO(null, "Nueva Sala", 1, "Desc");

        assertThrows(ResourceNotFoundException.class, () -> consultRoomService.updateConsultRoom(1L, update));
    }

    @Test
    void shouldDeleteConsultRoom() {
        ConsultRoom room = new ConsultRoom(1L, "Sala A", 1, "Desc");
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(room));

        consultRoomService.deleteConsultRoom(1L);

        verify(consultRoomRepository).delete(room);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentConsultRoom() {
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> consultRoomService.deleteConsultRoom(1L));
    }
}
