package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.PatientDTO;
import com.unimag.proyect.entities.Patient;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.unimag.proyect.mappers.PatientMapper;
import org.springframework.stereotype.Service;
import com.unimag.proyect.repositories.PatientRepository;
import com.unimag.proyect.services.PatientService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = patientMapper.toEntity(patientDTO);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toDTO(saved);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDTO)
                .toList();
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encotrado con ID: " + id));
        return patientMapper.toDTO(patient);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));

        existing.setFullName(patientDTO.getFullName());
        existing.setEmail(patientDTO.getEmail());
        existing.setPhone(patientDTO.getPhone());

        return patientMapper.toDTO(patientRepository.save(existing));
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encotrado con ID: " + id));
        patientRepository.delete(patient);
    }

}
