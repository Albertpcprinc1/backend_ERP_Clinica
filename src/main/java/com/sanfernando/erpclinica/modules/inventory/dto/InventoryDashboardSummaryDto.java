package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public record InventoryDashboardSummaryDto(
        Long totalMedicamentosActivos,
        Long totalLotesActivos,
        Long lotesAgotados,
        Long lotesStockBajo,
        Long lotesPorVencer,
        Long lotesVencidos,
        BigDecimal stockTotalDisponible,
        List<DashboardKardexItemDto> ultimosMovimientos
) {
}