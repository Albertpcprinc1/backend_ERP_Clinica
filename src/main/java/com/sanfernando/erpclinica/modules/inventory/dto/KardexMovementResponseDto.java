package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record KardexMovementResponseDto(
        Long id,
        Long medicamentoId,
        String medicamentoNombre,
        String dciNombre,
        Long loteId,
        String numeroLote,
        LocalDate fechaVencimiento,
        String tipoMovimiento,
        BigDecimal cantidad,
        BigDecimal stockAnterior,
        BigDecimal stockPosterior,
        String referenciaTipo,
        Long referenciaId,
        String motivo,
        String usuarioResponsable,
        LocalDateTime fechaMovimiento
) {
}