package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicineResponseDto(
        Long id,
        Long dciId,
        String dciNombre,
        Long categoriaId,
        String categoriaNombre,
        Long laboratorioId,
        String laboratorioNombre,
        Long unidadMedidaId,
        String unidadMedidaCodigo,
        String unidadMedidaNombre,
        String nombreComercial,
        String concentracion,
        String formaFarmaceutica,
        String presentacionComercial,
        String unidadPresentacion,
        BigDecimal factorConversionUnidadBase,
        String registroSanitario,
        Boolean esGenerico,
        Boolean requiereReceta,
        Boolean requiereRecetaArchivada,
        BigDecimal precioPublico,
        BigDecimal precioAseguradora,
        BigDecimal stockMinimoTotal,
        String observaciones,
        Boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}