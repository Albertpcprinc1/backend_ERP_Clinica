package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockOutputLineResponseDto(
        Long loteId,
        String numeroLote,
        LocalDate fechaVencimiento,
        Long inventarioLoteId,
        BigDecimal cantidadDescontada,
        BigDecimal stockAnterior,
        BigDecimal stockPosterior,
        BigDecimal stockDisponiblePosterior,
        Long kardexId
) {
}