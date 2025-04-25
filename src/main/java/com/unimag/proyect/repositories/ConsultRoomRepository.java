package com.unimag.proyect.repositories;

import com.unimag.proyect.entities.ConsultRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultRoomRepository extends JpaRepository<ConsultRoom, Long> {
    List<ConsultRoom> findByFloor(int floor);

    List<ConsultRoom> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a.consultRoom FROM Appointment a " +
            "WHERE a.consultRoom.id = :roomId " +
            "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    List<ConsultRoom> findRoomWithConflicts(@Param("roomId") Long roomId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
}



