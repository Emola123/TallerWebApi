package com.unimag.proyect.repositories;

import com.unimag.proyect.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND " +
            "(a.startTime < :endTime AND a.endTime > :startTime)")
    List<Appointment> findConflictsForDoctor(@Param("doctorId") Long doctorId,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Appointment a WHERE a.consultRoom.id = :roomId AND " +
            "(a.startTime < :endTime AND a.endTime > :startTime)")
    List<Appointment> findConflictsForRoom(@Param("roomId") Long roomId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND " +
            "CAST(a.startTime AS DATE) = :date")
    List<Appointment> findByDoctorIdAndDate(@Param("doctorId") Long doctorId,
                                            @Param("date") LocalDate date);
    
}
