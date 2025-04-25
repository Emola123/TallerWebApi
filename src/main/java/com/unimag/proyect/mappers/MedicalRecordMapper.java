package com.unimag.proyect.mappers;

import com.unimag.proyect.dtos.MedicalRecordDTO;
import com.unimag.proyect.entities.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "patient.id", target = "patientId")
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    MedicalRecord toEntity(MedicalRecordDTO medicalRecordDTO);
}
