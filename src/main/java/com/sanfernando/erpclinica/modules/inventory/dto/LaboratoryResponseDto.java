package com.sanfernando.erpclinica.modules.inventory.dto;

import java.time.LocalDateTime;

public record LaboratoryResponseDto(
        Long id,
        String nombre,
        String ruc,
        String paisOrigen,
        String telefono,
        String email,
        String direccion,
        Boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}