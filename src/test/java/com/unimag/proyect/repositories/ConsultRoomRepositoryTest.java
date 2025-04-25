package com.unimag.proyect.repositories;

import com.unimag.proyect.entities.ConsultRoom;
import com.unimag.proyect.entities.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class ConsultRoomRepositoryTest {

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void shouldDetectConflictingAppointments() {
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio A")
                .floor(1)
                .description("Consultorio principal con vista al parque")
                .build());

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(9);
        LocalDateTime end = start.plusHours(2);

        appointmentRepository.save(Appointment.builder()
                .consultRoom(room)
                .startTime(start)
                .endTime(end)
                .build());

        List<ConsultRoom> conflicts = consultRoomRepository.findRoomWithConflicts(room.getId(),
                start.plusMinutes(30), end.plusHours(1));

        assertFalse(conflicts.isEmpty());
    }

    @Test
    void shouldSaveConsultRoomWithoutConflict() {
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio B")
                .floor(2)
                .description("Consultorio pequeño en el segundo piso")
                .build());

        assertNotNull(room.getId());
        assertEquals("Consultorio B", room.getName());
        assertEquals(2, room.getFloor());
        assertEquals("Consultorio pequeño en el segundo piso", room.getDescription());
    }

    @Test
    void shouldFindConsultRoomByFloor() {
        ConsultRoom room1 = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio C")
                .floor(3)
                .description("Consultorio con equipos de alta tecnología")
                .build());
        ConsultRoom room2 = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio D")
                .floor(3)
                .description("Consultorio para consulta general")
                .build());

        List<ConsultRoom> rooms = consultRoomRepository.findByFloor(3);

        assertEquals(2, rooms.size());
    }

    @Test
    void shouldFindConsultRoomByName() {
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio E")
                .floor(2)
                .description("Consultorio destinado a especialidades")
                .build());

        List<ConsultRoom> rooms = consultRoomRepository.findByNameContainingIgnoreCase("consultorio");

        assertFalse(rooms.isEmpty());
        assertEquals("Consultorio E", rooms.get(0).getName());
    }

    @Test
    void shouldDeleteConsultRoom() {
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio F")
                .floor(1)
                .description("Consultorio de uso general")
                .build());

        Long id = room.getId();
        consultRoomRepository.deleteById(id);

        assertFalse(consultRoomRepository.findById(id).isPresent());
    }
}
