package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.MedicalRecordDTO;
import com.unimag.proyect.entities.Appointment;
import com.unimag.proyect.entities.MedicalRecord;
import com.unimag.proyect.entities.Patient;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import com.unimag.proyect.mappers.MedicalRecordMapper;
import com.unimag.proyect.repositories.AppointmentRepository;
import com.unimag.proyect.repositories.MedicalRecordRepository;
import com.unimag.proyect.repositories.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    @Mock private MedicalRecordRepository medicalRecordRepository;
    @Mock private MedicalRecordMapper medicalRecordMapper;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @Test
    void shouldCreateMedicalRecord() {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setAppointmentId(1L);
        dto.setPatientId(1L);
        dto.setDiagnosis("Dolor de cabeza");
        dto.setNotes("Paciente refiere dolor leve.");

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("COMPLETED");

        Patient patient = new Patient();
        patient.setId(1L);

        MedicalRecord record = new MedicalRecord();
        record.setDiagnosis("Dolor de cabeza");
        record.setNotes("Paciente refiere dolor leve.");
        record.setCreatedAt(LocalDateTime.now());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(record);
        when(medicalRecordMapper.toDTO(any(MedicalRecord.class))).thenReturn(dto);

        MedicalRecordDTO result = medicalRecordService.createMedicalRecord(dto);

        assertEquals("Dolor de cabeza", result.getDiagnosis());
    }

    @Test
    void shouldThrowWhenAppointmentNotCompleted() {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setAppointmentId(1L);
        dto.setPatientId(1L);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("PENDING");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(IllegalStateException.class, () -> medicalRecordService.createMedicalRecord(dto));
    }

    @Test
    void shouldReturnAllMedicalRecords() {
        MedicalRecord record1 = new MedicalRecord();
        record1.setId(1L);
        MedicalRecord record2 = new MedicalRecord();
        record2.setId(2L);

        MedicalRecordDTO dto1 = new MedicalRecordDTO();
        dto1.setDiagnosis("Diagnóstico 1");

        MedicalRecordDTO dto2 = new MedicalRecordDTO();
        dto2.setDiagnosis("Diagnóstico 2");

        List<MedicalRecord> records = List.of(record1, record2);

        when(medicalRecordRepository.findAll()).thenReturn(records);
        when(medicalRecordMapper.toDTO(record1)).thenReturn(dto1);
        when(medicalRecordMapper.toDTO(record2)).thenReturn(dto2);

        List<MedicalRecordDTO> result = medicalRecordService.getAllMedicalRecords();

        assertEquals(2, result.size());
        assertEquals("Diagnóstico 1", result.get(0).getDiagnosis());
        assertEquals("Diagnóstico 2", result.get(1).getDiagnosis());
    }


    @Test
    void shouldThrowWhenMedicalRecordNotFound() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> medicalRecordService.getMedicalRecordById(1L));
    }

    @Test
    void shouldUpdateMedicalRecord() {
        MedicalRecord existing = new MedicalRecord();
        existing.setId(1L);
        existing.setDiagnosis("Viejo");
        existing.setNotes("Nota vieja");

        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setDiagnosis("Nuevo");
        dto.setNotes("Nota nueva");

        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(medicalRecordRepository.save(any())).thenReturn(existing);
        when(medicalRecordMapper.toDTO(any())).thenReturn(dto);

        MedicalRecordDTO result = medicalRecordService.updateMedicalRecord(1L, dto);

        assertEquals("Nuevo", result.getDiagnosis());
        assertEquals("Nota nueva", result.getNotes());
    }

    @Test
    void shouldDeleteMedicalRecord() {
        MedicalRecord existing = new MedicalRecord();
        existing.setId(1L);

        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> medicalRecordService.deleteMedicalRecord(1L));
    }
}
