package com.unimag.proyect.mappers;

import com.unimag.proyect.dtos.DoctorDTO;
import com.unimag.proyect.entities.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDto(Doctor doctor);
    Doctor toEntity(DoctorDTO dto);
}
