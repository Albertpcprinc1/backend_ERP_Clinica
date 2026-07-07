package com.sanfernando.erpclinica.modules.inventory.dto;

import java.time.LocalDateTime;

public record ProviderResponseDto(
        Long id,
        String razonSocial,
        String ruc,
        String telefono,
        String email,
        String direccion,
        String contacto,
        Boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}