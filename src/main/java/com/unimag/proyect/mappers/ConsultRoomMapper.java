package com.unimag.proyect.mappers;

import com.unimag.proyect.dtos.ConsultRoomDTO;
import com.unimag.proyect.entities.ConsultRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultRoomMapper {
    ConsultRoomDTO toDTO(ConsultRoom consultRoom);
    ConsultRoom toEntity(ConsultRoomDTO consultRoomDTO);
}
