package com.unimag.proyect.mappers;

import com.unimag.proyect.dtos.PatientDTO;
import com.unimag.proyect.entities.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toDTO(Patient patient);
    Patient toEntity(PatientDTO patientDTO);
}
