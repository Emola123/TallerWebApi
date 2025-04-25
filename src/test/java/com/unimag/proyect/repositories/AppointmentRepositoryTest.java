package com.unimag.proyect.repositories;

import com.unimag.proyect.entities.Appointment;
import com.unimag.proyect.entities.ConsultRoom;
import com.unimag.proyect.entities.Doctor;
import com.unimag.proyect.entities.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    private Doctor createDoctor(String name, String email) {
        return doctorRepository.save(Doctor.builder()
                .fullName(name)
                .email(email)
                .speciality("General")
                .avalibleFrom(LocalTime.of(8, 0))
                .avalibleTo(LocalTime.of(18, 0))
                .build());
    }

    private Patient createPatient(String name, String email) {
        return patientRepository.save(Patient.builder()
                .fullName(name)
                .email(email)
                .phone("1234567890")
                .build());
    }

    private ConsultRoom createRoom(String name) {
        return consultRoomRepository.save(ConsultRoom.builder()
                .name(name)
                .floor(1)
                .description("Standard room")
                .build());
    }

    @Test
    void shouldDetectDoctorAppointmentConflict() {
        Doctor doctor = createDoctor("Dr. House", "house@test.com");
        Patient patient = createPatient("Gregory", "gregory@test.com");
        ConsultRoom room = createRoom("Room 1");

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        LocalDateTime end = start.plusHours(1);

        appointmentRepository.save(Appointment.builder()
                .doctor(doctor).patient(patient).consultRoom(room)
                .startTime(start).endTime(end).status("Scheduled").build());

        List<Appointment> conflicts = appointmentRepository.findConflictsForDoctor(
                doctor.getId(), start.plusMinutes(30), end.plusHours(1));

        assertFalse(conflicts.isEmpty());
    }

    @Test
    void shouldDetectRoomAppointmentConflict() {
        Doctor doctor = createDoctor("Dr. Grey", "grey@test.com");
        Patient patient = createPatient("Meredith", "meredith@test.com");
        ConsultRoom room = createRoom("Room 2");

        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(11);
        LocalDateTime end = start.plusHours(1);

        appointmentRepository.save(Appointment.builder()
                .doctor(doctor).patient(patient).consultRoom(room)
                .startTime(start).endTime(end).status("Confirmed").build());

        List<Appointment> conflicts = appointmentRepository.findConflictsForRoom(
                room.getId(), start.minusMinutes(30), end.minusMinutes(15));

        assertFalse(conflicts.isEmpty());
    }

    @Test
    void shouldSaveAppointmentWithoutConflict() {
        Doctor doctor = createDoctor("Dr. Watson", "watson@test.com");
        Patient patient = createPatient("John", "john@test.com");
        ConsultRoom room = createRoom("Room 3");

        LocalDateTime start = LocalDateTime.now().plusDays(3).withHour(13);
        LocalDateTime end = start.plusHours(1);

        Appointment appointment = Appointment.builder()
                .doctor(doctor).patient(patient).consultRoom(room)
                .startTime(start).endTime(end).status("Active").build();

        Appointment saved = appointmentRepository.save(appointment);

        assertNotNull(saved.getId());
        assertEquals("Active", saved.getStatus());
    }

    @Test
    void shouldFindAppointmentById() {
        Doctor doctor = createDoctor("Dr. Strange", "strange@test.com");
        Patient patient = createPatient("Peter Parker", "peter@test.com");
        ConsultRoom room = createRoom("Room 4");

        LocalDateTime start = LocalDateTime.now().plusDays(4).withHour(15);
        LocalDateTime end = start.plusHours(1);

        Appointment appointment = appointmentRepository.save(Appointment.builder()
                .doctor(doctor).patient(patient).consultRoom(room)
                .startTime(start).endTime(end).status("Confirmed").build());

        Appointment found = appointmentRepository.findById(appointment.getId()).orElseThrow();
        assertEquals(appointment.getId(), found.getId());
    }

    @Test
    void shouldDeleteAppointment() {
        Doctor doctor = createDoctor("Dr. Who", "who@test.com");
        Patient patient = createPatient("Amy Pond", "amy@test.com");
        ConsultRoom room = createRoom("TARDIS");

        LocalDateTime start = LocalDateTime.now().plusDays(5).withHour(9);
        LocalDateTime end = start.plusHours(2);

        Appointment appointment = appointmentRepository.save(Appointment.builder()
                .doctor(doctor).patient(patient).consultRoom(room)
                .startTime(start).endTime(end).status("Scheduled").build());

        Long id = appointment.getId();
        appointmentRepository.deleteById(id);

        assertFalse(appointmentRepository.findById(id).isPresent());
    }

    @Test
    void shouldFindAppointmentsByDoctorAndDate() {
        Doctor doctor = createDoctor("Dr. Doom", "doom@test.com");
        Patient patient = createPatient("Victor", "victor@test.com");
        ConsultRoom room = createRoom("Room 5");

        LocalDateTime start = LocalDateTime.now().plusDays(6).withHour(10);
        LocalDateTime end = start.plusHours(1);
        LocalDate date = start.toLocalDate();

        appointmentRepository.save(Appointment.builder()
                .doctor(doctor).patient(patient).consultRoom(room)
                .startTime(start).endTime(end).status("Scheduled").build());

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctor.getId(), date);
        assertFalse(appointments.isEmpty());
    }
}
