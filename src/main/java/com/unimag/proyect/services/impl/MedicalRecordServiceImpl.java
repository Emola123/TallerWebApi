package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.MedicalRecordDTO;
import com.unimag.proyect.entities.Appointment;
import com.unimag.proyect.entities.MedicalRecord;
import com.unimag.proyect.entities.Patient;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.unimag.proyect.mappers.MedicalRecordMapper;
import org.springframework.stereotype.Service;
import com.unimag.proyect.repositories.AppointmentRepository;
import com.unimag.proyect.repositories.MedicalRecordRepository;
import com.unimag.proyect.repositories.PatientRepository;
import com.unimag.proyect.services.MedicalRecordService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + dto.getAppointmentId()));

        if (!"COMPLETED".equalsIgnoreCase(appointment.getStatus())) {
            throw new IllegalStateException("Solo se puede registrar historial si la cita fue completada.");
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + dto.getPatientId()));

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setPatient(patient);
        record.setDiagnosis(dto.getDiagnosis());
        record.setNotes(dto.getNotes());
        record.setCreatedAt(LocalDateTime.now());

        return medicalRecordMapper.toDTO(medicalRecordRepository.save(record));
    }

    @Override
    public List<MedicalRecordDTO> getAllMedicalRecords(){
        return medicalRecordRepository.findAll().stream()
                .map(medicalRecordMapper::toDTO)
                .toList();
    }

    @Override
    public  MedicalRecordDTO getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Record no encontrado con ID: " + id));
        return medicalRecordMapper.toDTO(medicalRecord);
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO dto) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado con ID: " + id));

        existing.setDiagnosis(dto.getDiagnosis());
        existing.setNotes(dto.getNotes());

        return medicalRecordMapper.toDTO(medicalRecordRepository.save(existing));
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado con ID: " + id));
        medicalRecordRepository.delete(medicalRecord);
    }

}
