package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.StockOutputLineResponseDto;
import com.sanfernando.erpclinica.modules.inventory.dto.StockOutputRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.StockOutputResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvInventarioLote;
import com.sanfernando.erpclinica.modules.inventory.entity.InvKardexMovimiento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLoteMedicamento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvMedicamentoComercial;
import com.sanfernando.erpclinica.modules.inventory.repository.InvInventarioLoteRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvKardexMovimientoRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvMedicamentoComercialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockOutputService {

    private static final String MOVIMIENTO_DEFAULT = "ENTREGA_INTERNA";

    private final InvMedicamentoComercialRepository medicamentoRepository;
    private final InvInventarioLoteRepository inventarioLoteRepository;
    private final InvKardexMovimientoRepository kardexRepository;

    public StockOutputService(
            InvMedicamentoComercialRepository medicamentoRepository,
            InvInventarioLoteRepository inventarioLoteRepository,
            InvKardexMovimientoRepository kardexRepository
    ) {
        this.medicamentoRepository = medicamentoRepository;
        this.inventarioLoteRepository = inventarioLoteRepository;
        this.kardexRepository = kardexRepository;
    }

    @Transactional
    public StockOutputResponseDto registerStockOutput(StockOutputRequestDto request) {
        InvMedicamentoComercial medicamento = medicamentoRepository.findById(request.medicamentoId())
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicamento comercial no encontrado o inactivo"));

        BigDecimal cantidadSolicitada = positiveRequired(request.cantidad(), "La cantidad solicitada debe ser mayor a cero");
        String tipoMovimiento = normalizeTipoMovimiento(request.tipoMovimiento());

        List<InvInventarioLote> lotesDisponibles = inventarioLoteRepository.findAvailableLotsByMedicineFefo(medicamento.getId());

        BigDecimal stockTotalDisponible = lotesDisponibles.stream()
                .map(this::stockDisponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (stockTotalDisponible.compareTo(cantidadSolicitada) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Stock insuficiente. Disponible: " + stockTotalDisponible + ", solicitado: " + cantidadSolicitada
            );
        }

        BigDecimal cantidadPendiente = cantidadSolicitada;
        BigDecimal cantidadAtendida = BigDecimal.ZERO;
        LocalDateTime fechaMovimiento = LocalDateTime.now();
        List<StockOutputLineResponseDto> lineas = new ArrayList<>();

        for (InvInventarioLote inventario : lotesDisponibles) {
            if (cantidadPendiente.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            InvLoteMedicamento lote = inventario.getLote();
            BigDecimal disponibleLote = stockDisponible(inventario);

            if (disponibleLote.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal cantidadADescontar = disponibleLote.min(cantidadPendiente);
            BigDecimal stockAnterior = inventario.getStockActual();
            BigDecimal stockPosterior = stockAnterior.subtract(cantidadADescontar);

            inventario.setStockActual(stockPosterior);
            InvInventarioLote inventarioGuardado = inventarioLoteRepository.save(inventario);

            InvKardexMovimiento kardex = new InvKardexMovimiento();
            kardex.setMedicamento(medicamento);
            kardex.setLote(lote);
            kardex.setTipoMovimiento(tipoMovimiento);
            kardex.setCantidad(cantidadADescontar);
            kardex.setStockAnterior(stockAnterior);
            kardex.setStockPosterior(stockPosterior);
            kardex.setReferenciaTipo(defaultText(request.referenciaTipo(), "SALIDA_STOCK"));
            kardex.setReferenciaId(request.referenciaId());
            kardex.setMotivo(defaultText(request.motivo(), "Salida de stock con regla FEFO"));
            kardex.setUsuarioResponsable(defaultText(request.usuarioResponsable(), "usuario_farmacia_local"));
            kardex.setFechaMovimiento(fechaMovimiento);

            InvKardexMovimiento kardexGuardado = kardexRepository.save(kardex);

            BigDecimal stockDisponiblePosterior = inventarioGuardado.getStockActual()
                    .subtract(inventarioGuardado.getStockComprometido());

            lineas.add(new StockOutputLineResponseDto(
                    lote.getId(),
                    lote.getNumeroLote(),
                    lote.getFechaVencimiento(),
                    inventarioGuardado.getId(),
                    cantidadADescontar,
                    stockAnterior,
                    stockPosterior,
                    stockDisponiblePosterior,
                    kardexGuardado.getId()
            ));

            cantidadPendiente = cantidadPendiente.subtract(cantidadADescontar);
            cantidadAtendida = cantidadAtendida.add(cantidadADescontar);
        }

        return new StockOutputResponseDto(
                medicamento.getId(),
                medicamento.getNombreComercial(),
                medicamento.getDci() != null ? medicamento.getDci().getNombreGenerico() : null,
                cantidadSolicitada,
                cantidadAtendida,
                tipoMovimiento,
                fechaMovimiento,
                lineas
        );
    }

    private BigDecimal stockDisponible(InvInventarioLote inventario) {
        BigDecimal stockActual = inventario.getStockActual() != null ? inventario.getStockActual() : BigDecimal.ZERO;
        BigDecimal stockComprometido = inventario.getStockComprometido() != null ? inventario.getStockComprometido() : BigDecimal.ZERO;
        return stockActual.subtract(stockComprometido);
    }

    private BigDecimal positiveRequired(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return value;
    }

    private String normalizeTipoMovimiento(String value) {
        String result = normalizeOptional(value);
        if (result == null) {
            return MOVIMIENTO_DEFAULT;
        }

        if (
                result.equals("VENTA_FARMACIA") ||
                result.equals("ENTREGA_INTERNA") ||
                result.equals("USO_INSUMO_MEDICO") ||
                result.equals("MERMA") ||
                result.equals("VENCIMIENTO") ||
                result.equals("TRANSFERENCIA") ||
                result.equals("AJUSTE_AUTORIZADO")
        ) {
            return result;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de movimiento no permitido para salida de stock");
    }

    private String normalizeOptional(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private String defaultText(String value, String defaultValue) {
        String normalized = normalizeOptional(value);
        return normalized != null ? normalized : defaultValue;
    }
}