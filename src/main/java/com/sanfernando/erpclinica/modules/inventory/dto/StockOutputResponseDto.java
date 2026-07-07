package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StockOutputResponseDto(
        Long medicamentoId,
        String medicamentoNombre,
        String dciNombre,
        BigDecimal cantidadSolicitada,
        BigDecimal cantidadAtendida,
        String tipoMovimiento,
        LocalDateTime fechaMovimiento,
        List<StockOutputLineResponseDto> lotesAfectados
) {
}