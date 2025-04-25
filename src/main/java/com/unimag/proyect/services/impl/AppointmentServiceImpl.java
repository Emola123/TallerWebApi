package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.AppointmentDTO;
import com.unimag.proyect.entities.Appointment;
import com.unimag.proyect.entities.Doctor;
import com.unimag.proyect.exceptions.ConflictException;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.unimag.proyect.mappers.AppointmentMapper;
import org.springframework.stereotype.Service;
import com.unimag.proyect.repositories.AppointmentRepository;
import com.unimag.proyect.repositories.ConsultRoomRepository;
import com.unimag.proyect.repositories.DoctorRepository;
import com.unimag.proyect.repositories.PatientRepository;
import com.unimag.proyect.services.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultRoomRepository consultRoomRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        LocalDateTime start = dto.getStartTime();
        LocalDateTime end = dto.getEndTime();

        if (start.isBefore(LocalDateTime.now())) {
            throw new ConflictException("No se puede agendar una cita en el pasado.");
        }

        if (end.isBefore(start)) {
            throw new ConflictException("La hora de fin debe ser posterior a la hora de inicio.");
        }

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));

        LocalDateTime availableFrom = start.with(doctor.getAvalibleFrom());
        LocalDateTime availableTo = start.with(doctor.getAvalibleTo());

        if (start.isBefore(availableFrom) || end.isAfter(availableTo)) {
            throw new ConflictException("La cita está fuera del horario disponible del doctor.");
        }

        boolean doctorConflict = !appointmentRepository
                .findConflictsForDoctor(dto.getDoctorId(), start, end).isEmpty();
        boolean roomConflict = !appointmentRepository
                .findConflictsForRoom(dto.getConsultRoomId(), start, end).isEmpty();

        if (doctorConflict || roomConflict) {
            throw new ConflictException("El doctor o el consultorio ya tienen una cita en ese horario.");
        }

        Appointment appointment = appointmentMapper.toEntity(dto);
        appointment.setPatient(patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado")));
        appointment.setDoctor(doctor);
        appointment.setConsultRoom(consultRoomRepository.findById(dto.getConsultRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado")));
        appointment.setStatus("SCHEDULED");

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentDTO> getAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }

    @Override
    public AppointmentDTO getAppointmentId(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (appointment.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede modificar una cita que ya ocurrió.");
        }

        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setStatus(dto.getStatus());

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        appointmentRepository.delete(appointment);
    }
}
