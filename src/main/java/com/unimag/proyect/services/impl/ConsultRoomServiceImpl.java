package com.unimag.proyect.services.impl;

import com.unimag.proyect.dtos.ConsultRoomDTO;
import com.unimag.proyect.entities.ConsultRoom;
import com.unimag.proyect.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.unimag.proyect.mappers.ConsultRoomMapper;
import org.springframework.stereotype.Service;
import com.unimag.proyect.repositories.ConsultRoomRepository;
import com.unimag.proyect.services.ConsultRoomService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultRoomServiceImpl implements ConsultRoomService {
    private final ConsultRoomRepository consultRoomRepository;
    private final ConsultRoomMapper consultRoomMapper;

    @Override
    public ConsultRoomDTO createConsultRoom(ConsultRoomDTO dto){
        ConsultRoom consultRoom = consultRoomMapper.toEntity(dto);
        ConsultRoom saved = consultRoomRepository.save(consultRoom);
        return consultRoomMapper.toDTO(saved);
    }

    @Override
    public List<ConsultRoomDTO> getAllConsultRooms() {
        return consultRoomRepository.findAll().stream()
                .map(consultRoomMapper::toDTO)
                .toList();
    }

    @Override
    public ConsultRoomDTO getConsultRoomById(Long id){
        ConsultRoom consultRoom = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID "+ id));
        return consultRoomMapper.toDTO(consultRoom);
    }

    @Override
    public ConsultRoomDTO updateConsultRoom(Long id, ConsultRoomDTO dto){
        ConsultRoom existing = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));

        existing.setName(dto.getName());
        existing.setFloor(dto.getFloor());
        existing.setDescription(dto.getDescription());

        return consultRoomMapper.toDTO(consultRoomRepository.save(existing));
    }

    @Override
    public void deleteConsultRoom(Long id){
        ConsultRoom c = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));
        consultRoomRepository.delete(c);

    }

}
