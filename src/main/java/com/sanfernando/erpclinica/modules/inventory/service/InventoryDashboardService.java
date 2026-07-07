package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.DashboardKardexItemDto;
import com.sanfernando.erpclinica.modules.inventory.dto.InventoryDashboardSummaryDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvInventarioLote;
import com.sanfernando.erpclinica.modules.inventory.entity.InvKardexMovimiento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLoteMedicamento;
import com.sanfernando.erpclinica.modules.inventory.repository.InvInventarioLoteRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvKardexMovimientoRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvLoteMedicamentoRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvMedicamentoComercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class InventoryDashboardService {

    private static final long DIAS_ALERTA_VENCIMIENTO = 90;

    private final InvMedicamentoComercialRepository medicamentoRepository;
    private final InvLoteMedicamentoRepository loteRepository;
    private final InvInventarioLoteRepository inventarioLoteRepository;
    private final InvKardexMovimientoRepository kardexRepository;

    public InventoryDashboardService(
            InvMedicamentoComercialRepository medicamentoRepository,
            InvLoteMedicamentoRepository loteRepository,
            InvInventarioLoteRepository inventarioLoteRepository,
            InvKardexMovimientoRepository kardexRepository
    ) {
        this.medicamentoRepository = medicamentoRepository;
        this.loteRepository = loteRepository;
        this.inventarioLoteRepository = inventarioLoteRepository;
        this.kardexRepository = kardexRepository;
    }

    @Transactional(readOnly = true)
    public InventoryDashboardSummaryDto getSummary() {
        List<InvInventarioLote> inventarios = inventarioLoteRepository.findByActivoTrueOrderByLoteFechaVencimientoAsc();

        Long totalMedicamentosActivos = (long) medicamentoRepository.findByActivoTrueOrderByNombreComercialAsc().size();
        Long totalLotesActivos = (long) loteRepository.findByActivoTrueOrderByFechaVencimientoAsc().size();

        BigDecimal stockTotalDisponible = inventarios.stream()
                .map(this::stockDisponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long lotesAgotados = inventarios.stream()
                .filter(item -> stockDisponible(item).compareTo(BigDecimal.ZERO) <= 0)
                .count();

        Long lotesStockBajo = inventarios.stream()
                .filter(item -> stockDisponible(item).compareTo(item.getStockMinimo()) <= 0)
                .count();

        Long lotesVencidos = inventarios.stream()
                .filter(this::estaVencido)
                .count();

        Long lotesPorVencer = inventarios.stream()
                .filter(this::estaPorVencer)
                .count();

        List<DashboardKardexItemDto> ultimosMovimientos = kardexRepository.findTop10ByOrderByFechaMovimientoDesc()
                .stream()
                .map(this::toKardexItem)
                .toList();

        return new InventoryDashboardSummaryDto(
                totalMedicamentosActivos,
                totalLotesActivos,
                lotesAgotados,
                lotesStockBajo,
                lotesPorVencer,
                lotesVencidos,
                stockTotalDisponible,
                ultimosMovimientos
        );
    }

    private BigDecimal stockDisponible(InvInventarioLote inventario) {
        if (inventario.getStockDisponible() != null) {
            return inventario.getStockDisponible();
        }

        BigDecimal stockActual = inventario.getStockActual() != null ? inventario.getStockActual() : BigDecimal.ZERO;
        BigDecimal stockComprometido = inventario.getStockComprometido() != null ? inventario.getStockComprometido() : BigDecimal.ZERO;

        return stockActual.subtract(stockComprometido);
    }

    private boolean estaVencido(InvInventarioLote inventario) {
        InvLoteMedicamento lote = inventario.getLote();
        return lote.getFechaVencimiento().isBefore(LocalDate.now());
    }

    private boolean estaPorVencer(InvInventarioLote inventario) {
        InvLoteMedicamento lote = inventario.getLote();
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), lote.getFechaVencimiento());

        return dias >= 0 && dias <= DIAS_ALERTA_VENCIMIENTO;
    }

    private DashboardKardexItemDto toKardexItem(InvKardexMovimiento entity) {
        String medicamentoNombre = entity.getMedicamento() != null
                ? entity.getMedicamento().getNombreComercial()
                : null;

        String numeroLote = entity.getLote() != null
                ? entity.getLote().getNumeroLote()
                : null;

        return new DashboardKardexItemDto(
                entity.getId(),
                medicamentoNombre,
                numeroLote,
                entity.getTipoMovimiento(),
                entity.getCantidad(),
                entity.getStockAnterior(),
                entity.getStockPosterior(),
                entity.getUsuarioResponsable(),
                entity.getFechaMovimiento()
        );
    }
}