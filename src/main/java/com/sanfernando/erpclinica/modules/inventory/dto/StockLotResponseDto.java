package com.sanfernando.erpclinica.modules.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockLotResponseDto(
        Long inventarioLoteId,
        Long loteId,
        Long medicamentoId,
        String medicamentoNombre,
        String dciNombre,
        String registroSanitario,
        String numeroLote,
        LocalDate fechaIngreso,
        LocalDate fechaVencimiento,
        Long diasParaVencer,
        Boolean vencido,
        Boolean alertaVencimiento,
        String estadoVencimiento,
        BigDecimal stockActual,
        BigDecimal stockComprometido,
        BigDecimal stockDisponible,
        BigDecimal stockMinimo,
        Boolean alertaStockBajo,
        Long proveedorId,
        String proveedorNombre,
        String ubicacionFisica
) {
}