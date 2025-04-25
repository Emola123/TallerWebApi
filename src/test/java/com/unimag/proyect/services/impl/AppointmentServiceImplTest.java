package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.AppointmentDTO;
import com.unimag.proyect.entities.Appointment;
import com.unimag.proyect.entities.ConsultRoom;
import com.unimag.proyect.entities.Doctor;
import com.unimag.proyect.entities.Patient;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import com.unimag.proyect.mappers.AppointmentMapper;
import com.unimag.proyect.repositories.AppointmentRepository;
import com.unimag.proyect.repositories.ConsultRoomRepository;
import com.unimag.proyect.repositories.DoctorRepository;
import com.unimag.proyect.repositories.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private ConsultRoomRepository consultRoomRepository;
    @Mock private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void shouldCreateAppointment() {
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                .build();

        Doctor doctor = Doctor.builder()
                .id(1L)
                .avalibleFrom(LocalTime.of(8, 0))
                .avalibleTo(LocalTime.of(17, 0))
                .build();

        Patient patient = Patient.builder().id(1L).build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).build();
        Appointment appointment = Appointment.builder().doctor(doctor).patient(patient).consultRoom(consultRoom).build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(appointmentRepository.findConflictsForDoctor(1L, dto.getStartTime(), dto.getEndTime())).thenReturn(List.of());
        when(appointmentRepository.findConflictsForRoom(1L, dto.getStartTime(), dto.getEndTime())).thenReturn(List.of());
        when(appointmentMapper.toEntity(dto)).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.toDTO(any())).thenReturn(dto);

        AppointmentDTO result = appointmentService.createAppointment(dto);

        assertEquals(1L, result.getPatientId());
    }

    @Test
    void shouldThrowIfDoctorNotFound() {
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createAppointment(dto));
    }

}
