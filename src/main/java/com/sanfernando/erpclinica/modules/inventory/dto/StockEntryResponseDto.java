package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StockEntryResponseDto(
        Long medicamentoId,
        String medicamentoNombre,
        Long loteId,
        String numeroLote,
        LocalDate fechaIngreso,
        LocalDate fechaVencimiento,
        Long inventarioLoteId,
        BigDecimal stockActual,
        BigDecimal stockComprometido,
        BigDecimal stockDisponible,
        BigDecimal stockMinimo,
        Long kardexId,
        String tipoMovimiento,
        BigDecimal cantidad,
        BigDecimal stockAnterior,
        BigDecimal stockPosterior,
        LocalDateTime fechaMovimiento
) {
}