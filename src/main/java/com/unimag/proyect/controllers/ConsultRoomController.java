package com.unimag.proyect.controllers;

import com.unimag.proyect.dtos.ConsultRoomDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unimag.proyect.services.ConsultRoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ConsultRoomController {
    private final ConsultRoomService consultRoomService;

    @PostMapping
    public ResponseEntity<ConsultRoomDTO> createRoom(@Valid @RequestBody ConsultRoomDTO room) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultRoomService.createConsultRoom(room));
    }

    @GetMapping
    public ResponseEntity<List<ConsultRoomDTO>> getAllRooms() {
        return ResponseEntity.ok(consultRoomService.getAllConsultRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultRoomDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(consultRoomService.getConsultRoomById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultRoomDTO> updateRoom(@PathVariable Long id, @Valid @RequestBody ConsultRoomDTO room) {
        return ResponseEntity.ok(consultRoomService.updateConsultRoom(id, room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        consultRoomService.deleteConsultRoom(id);
        return ResponseEntity.noContent().build();
    }

}
