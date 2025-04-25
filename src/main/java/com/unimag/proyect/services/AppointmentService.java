package com.unimag.proyect.services;

import com.unimag.proyect.dtos.AppointmentDTO;

import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO dto);

    List<AppointmentDTO> getAppointments();

    AppointmentDTO getAppointmentId(Long id);

    AppointmentDTO updateAppointment(Long id, AppointmentDTO dto);

    void deleteAppointment(Long id);
}
