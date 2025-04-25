package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.PatientDTO;
import com.unimag.proyect.entities.Patient;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import com.unimag.proyect.mappers.PatientMapper;
import com.unimag.proyect.repositories.PatientRepository;
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
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void shouldCreatePatient() {
        PatientDTO dto = new PatientDTO(null, "Juan Pérez", "juan@example.com", "123456789");
        Patient entity = new Patient(null, "Juan Pérez", "juan@example.com", "123456789");

        when(patientMapper.toEntity(dto)).thenReturn(entity);
        when(patientRepository.save(entity)).thenReturn(entity);
        when(patientMapper.toDTO(entity)).thenReturn(dto);

        PatientDTO result = patientService.createPatient(dto);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.getFullName());
    }

    @Test
    void shouldReturnAllPatients() {
        Patient patient1 = new Patient(1L, "Ana", "ana@mail.com", "111");
        Patient patient2 = new Patient(2L, "Carlos", "carlos@mail.com", "222");

        PatientDTO dto1 = new PatientDTO(1L, "Ana", "ana@mail.com", "111");
        PatientDTO dto2 = new PatientDTO(2L, "Carlos", "carlos@mail.com", "222");

        when(patientRepository.findAll()).thenReturn(List.of(patient1, patient2));
        when(patientMapper.toDTO(patient1)).thenReturn(dto1);
        when(patientMapper.toDTO(patient2)).thenReturn(dto2);

        List<PatientDTO> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        assertEquals("Carlos", result.get(1).getFullName());
    }

    @Test
    void shouldGetPatientById() {
        Patient patient = new Patient(1L, "Laura", "laura@mail.com", "333");
        PatientDTO dto = new PatientDTO(1L, "Laura", "laura@mail.com", "333");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDTO(patient)).thenReturn(dto);

        PatientDTO result = patientService.getPatientById(1L);

        assertEquals("Laura", result.getFullName());
    }

    @Test
    void shouldThrowWhenPatientNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(99L));
    }

    @Test
    void shouldUpdatePatient() {
        Patient existing = new Patient(1L, "Carlos", "old@mail.com", "000");
        PatientDTO updateDto = new PatientDTO(1L, "Carlos Nuevo", "nuevo@mail.com", "123456");

        Patient updated = new Patient(1L, "Carlos Nuevo", "nuevo@mail.com", "123456");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(existing)).thenReturn(updated);
        when(patientMapper.toDTO(updated)).thenReturn(updateDto);

        PatientDTO result = patientService.updatePatient(1L, updateDto);

        assertEquals("Carlos Nuevo", result.getFullName());
        assertEquals("nuevo@mail.com", result.getEmail());
    }

    @Test
    void shouldDeletePatient() {
        Patient patient = new Patient(1L, "Pedro", "pedro@mail.com", "999");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        assertDoesNotThrow(() -> patientService.deletePatient(1L));
        verify(patientRepository).delete(patient);
    }
}
