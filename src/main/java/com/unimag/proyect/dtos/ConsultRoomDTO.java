package com.unimag.proyect.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultRoomDTO {
    private Long id;
    private String name;
    private int floor;
    private String description;
}
