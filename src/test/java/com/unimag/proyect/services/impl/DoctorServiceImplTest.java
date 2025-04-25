package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.DoctorDTO;
import com.unimag.proyect.entities.Doctor;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import com.unimag.proyect.mappers.DoctorMapper;
import com.unimag.proyect.repositories.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock private DoctorRepository doctorRepository;
    @Mock private DoctorMapper doctorMapper;

    @InjectMocks private DoctorServiceImpl doctorService;

    @Test
    void shouldCreateDoctor() {
        DoctorDTO dto = new DoctorDTO(null, "Juan Pérez", "juan@example.com", "Cardiología",
                LocalTime.of(8, 0), LocalTime.of(14, 0));
        Doctor entity = new Doctor(null, "Juan Pérez", "juan@example.com", "Cardiología",
                LocalTime.of(8, 0), LocalTime.of(14, 0));
        Doctor saved = new Doctor(1L, "Juan Pérez", "juan@example.com", "Cardiología",
                LocalTime.of(8, 0), LocalTime.of(14, 0));
        DoctorDTO savedDto = new DoctorDTO(1L, "Juan Pérez", "juan@example.com", "Cardiología",
                LocalTime.of(8, 0), LocalTime.of(14, 0));

        when(doctorMapper.toEntity(dto)).thenReturn(entity);
        when(doctorRepository.save(entity)).thenReturn(saved);
        when(doctorMapper.toDto(saved)).thenReturn(savedDto);

        DoctorDTO result = doctorService.createDoctor(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void shouldReturnAllDoctors() {
        List<Doctor> doctors = List.of(
                new Doctor(1L, "Doc 1", "doc1@example.com", "Pediatría", LocalTime.of(8, 0), LocalTime.of(12, 0)),
                new Doctor(2L, "Doc 2", "doc2@example.com", "Cirugía", LocalTime.of(9, 0), LocalTime.of(13, 0))
        );

        List<DoctorDTO> dtos = List.of(
                new DoctorDTO(1L, "Doc 1", "doc1@example.com", "Pediatría", LocalTime.of(8, 0), LocalTime.of(12, 0)),
                new DoctorDTO(2L, "Doc 2", "doc2@example.com", "Cirugía", LocalTime.of(9, 0), LocalTime.of(13, 0))
        );

        when(doctorRepository.findAll()).thenReturn(doctors);
        when(doctorMapper.toDto(doctors.get(0))).thenReturn(dtos.get(0));
        when(doctorMapper.toDto(doctors.get(1))).thenReturn(dtos.get(1));

        List<DoctorDTO> result = doctorService.getAllDoctors();

        assertEquals(dtos, result);
    }

    @Test
    void shouldReturnDoctorById() {
        Doctor doctor = new Doctor(1L, "Doc", "doc@example.com", "General", LocalTime.of(8, 0), LocalTime.of(16, 0));
        DoctorDTO dto = new DoctorDTO(1L, "Doc", "doc@example.com", "General", LocalTime.of(8, 0), LocalTime.of(16, 0));

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        DoctorDTO result = doctorService.getDoctorById(1L);

        assertEquals(dto, result);
    }

    @Test
    void shouldThrowWhenDoctorNotFoundById() {
        when(doctorRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(100L));
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor existing = new Doctor(1L, "Old", "old@example.com", "Trauma",
                LocalTime.of(7, 0), LocalTime.of(13, 0));
        DoctorDTO update = new DoctorDTO(null, "New", "new@example.com", "Ortopedia",
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        Doctor updated = new Doctor(1L, "New", "new@example.com", "Ortopedia",
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        DoctorDTO updatedDto = new DoctorDTO(1L, "New", "new@example.com", "Ortopedia",
                LocalTime.of(9, 0), LocalTime.of(17, 0));

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(doctorRepository.save(existing)).thenReturn(updated);
        when(doctorMapper.toDto(updated)).thenReturn(updatedDto);

        DoctorDTO result = doctorService.updateDoctor(1L, update);

        assertEquals(updatedDto, result);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        DoctorDTO update = new DoctorDTO(null, "Nombre", "email@example.com", "Especialidad",
                LocalTime.of(9, 0), LocalTime.of(12, 0));

        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateDoctor(1L, update));
    }

    @Test
    void shouldDeleteDoctor() {
        Doctor doctor = new Doctor(1L, "Eliminar", "mail@example.com", "Neuro", LocalTime.of(7, 0), LocalTime.of(15, 0));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(1L);

        verify(doctorRepository).delete(doctor);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.deleteDoctor(1L));
    }
}
