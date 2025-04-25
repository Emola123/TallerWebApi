package com.unimag.proyect.services;

import com.unimag.proyect.dtos.DoctorDTO;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO dto);
    List<DoctorDTO> getAllDoctors();
    DoctorDTO getDoctorById(Long id);
    DoctorDTO updateDoctor(Long id, DoctorDTO dto);
    void deleteDoctor(Long id);
}
