package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;

public record DciStockSummaryDto(
        Long dciId,
        String nombreGenerico,
        String descripcion,
        Long totalMedicamentosComerciales,
        Long totalLotesActivos,
        BigDecimal stockTotalDisponible,
        Boolean activo
) {
}