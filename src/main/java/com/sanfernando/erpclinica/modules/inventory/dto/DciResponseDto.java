package com.sanfernando.erpclinica.modules.inventory.dto;

import java.time.LocalDateTime;

public record DciResponseDto(
        Long id,
        String nombreGenerico,
        String descripcion,
        Boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}