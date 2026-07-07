package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DashboardKardexItemDto(
        Long id,
        String medicamentoNombre,
        String numeroLote,
        String tipoMovimiento,
        BigDecimal cantidad,
        BigDecimal stockAnterior,
        BigDecimal stockPosterior,
        String usuarioResponsable,
        LocalDateTime fechaMovimiento
) {
}