package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.StockLotResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvDci;
import com.sanfernando.erpclinica.modules.inventory.entity.InvInventarioLote;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLaboratorioFabricante;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLoteMedicamento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvMedicamentoComercial;
import com.sanfernando.erpclinica.modules.inventory.entity.InvProveedor;
import com.sanfernando.erpclinica.modules.inventory.repository.InvInventarioLoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class StockQueryService {

    private static final long DIAS_ALERTA_VENCIMIENTO = 90;

    private final InvInventarioLoteRepository inventarioLoteRepository;

    public StockQueryService(InvInventarioLoteRepository inventarioLoteRepository) {
        this.inventarioLoteRepository = inventarioLoteRepository;
    }

    @Transactional(readOnly = true)
    public List<StockLotResponseDto> findAllStocksFefo() {
        return inventarioLoteRepository.findByActivoTrueOrderByLoteFechaVencimientoAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private StockLotResponseDto toResponse(InvInventarioLote inventario) {
        InvLoteMedicamento lote = inventario.getLote();
        InvMedicamentoComercial medicamento = lote.getMedicamento();
        InvDci dci = medicamento.getDci();
        InvProveedor proveedor = lote.getProveedor();

        BigDecimal stockDisponible = inventario.getStockDisponible();
        if (stockDisponible == null) {
            stockDisponible = inventario.getStockActual().subtract(inventario.getStockComprometido());
        }

        boolean alertaStockBajo = stockDisponible.compareTo(inventario.getStockMinimo()) <= 0;

        LocalDate hoy = LocalDate.now();
        long diasParaVencer = ChronoUnit.DAYS.between(hoy, lote.getFechaVencimiento());
        boolean vencido = diasParaVencer < 0;
        boolean alertaVencimiento = diasParaVencer <= DIAS_ALERTA_VENCIMIENTO;

        String estadoVencimiento;
        if (vencido) {
            estadoVencimiento = "VENCIDO";
        } else if (alertaVencimiento) {
            estadoVencimiento = "POR_VENCER";
        } else {
            estadoVencimiento = "VIGENTE";
        }

        return new StockLotResponseDto(
                inventario.getId(),
                lote.getId(),
                medicamento.getId(),
                medicamento.getNombreComercial(),
                dci != null ? dci.getNombreGenerico() : null,
                medicamento.getRegistroSanitario(),
                lote.getNumeroLote(),
                lote.getFechaIngreso(),
                lote.getFechaVencimiento(),
                diasParaVencer,
                vencido,
                alertaVencimiento,
                estadoVencimiento,
                inventario.getStockActual(),
                inventario.getStockComprometido(),
                stockDisponible,
                inventario.getStockMinimo(),
                alertaStockBajo,
                proveedor != null ? proveedor.getId() : null,
                proveedor != null ? proveedor.getRazonSocial() : null,
                lote.getUbicacionFisica()
        );
    }
}