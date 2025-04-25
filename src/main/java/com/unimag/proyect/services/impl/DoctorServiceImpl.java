package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.DoctorDTO;
import com.unimag.proyect.entities.Doctor;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.unimag.proyect.mappers.DoctorMapper;
import org.springframework.stereotype.Service;
import com.unimag.proyect.repositories.DoctorRepository;
import com.unimag.proyect.services.DoctorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toDto(saved);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .toList();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al doctor con id: " + id));
        return doctorMapper.toDto(doctor);
    }

    @Override
    public DoctorDTO updateDoctor(Long id, DoctorDTO dto){
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID" + id));

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setSpeciality(dto.getSpeciality());
        existing.setAvalibleFrom(dto.getAvalibleFrom());
        existing.setAvalibleTo(dto.getAvalibleTo());

        return doctorMapper.toDto(doctorRepository.save(existing));
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID" + id));
        doctorRepository.delete(doctor);
    }


}
