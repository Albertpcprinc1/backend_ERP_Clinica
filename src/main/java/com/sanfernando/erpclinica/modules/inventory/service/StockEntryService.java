package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.StockEntryRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.StockEntryResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvInventarioLote;
import com.sanfernando.erpclinica.modules.inventory.entity.InvKardexMovimiento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLoteMedicamento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvMedicamentoComercial;
import com.sanfernando.erpclinica.modules.inventory.entity.InvProveedor;
import com.sanfernando.erpclinica.modules.inventory.repository.InvInventarioLoteRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvKardexMovimientoRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvLoteMedicamentoRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvMedicamentoComercialRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class StockEntryService {

    private static final String MOVIMIENTO_INGRESO_PROVEEDOR = "INGRESO_PROVEEDOR";

    private final InvMedicamentoComercialRepository medicamentoRepository;
    private final InvProveedorRepository proveedorRepository;
    private final InvLoteMedicamentoRepository loteRepository;
    private final InvInventarioLoteRepository inventarioLoteRepository;
    private final InvKardexMovimientoRepository kardexRepository;

    public StockEntryService(
            InvMedicamentoComercialRepository medicamentoRepository,
            InvProveedorRepository proveedorRepository,
            InvLoteMedicamentoRepository loteRepository,
            InvInventarioLoteRepository inventarioLoteRepository,
            InvKardexMovimientoRepository kardexRepository
    ) {
        this.medicamentoRepository = medicamentoRepository;
        this.proveedorRepository = proveedorRepository;
        this.loteRepository = loteRepository;
        this.inventarioLoteRepository = inventarioLoteRepository;
        this.kardexRepository = kardexRepository;
    }

    @Transactional
    public StockEntryResponseDto registerStockEntry(StockEntryRequestDto request) {
        InvMedicamentoComercial medicamento = medicamentoRepository.findById(request.medicamentoId())
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicamento comercial no encontrado o inactivo"));

        InvProveedor proveedor = null;
        if (request.proveedorId() != null) {
            proveedor = proveedorRepository.findById(request.proveedorId())
                    .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proveedor no encontrado o inactivo"));
        }

        String numeroLote = normalizeRequired(request.numeroLote(), "El numero de lote es obligatorio");
        BigDecimal cantidad = positiveRequired(request.cantidad(), "La cantidad ingresada debe ser mayor a cero");
        BigDecimal costoUnitario = moneyOrZero(request.costoUnitario(), "El costo unitario no puede ser negativo");
        BigDecimal stockMinimo = moneyOrZero(request.stockMinimo(), "El stock minimo del lote no puede ser negativo");

        LocalDate fechaIngreso = request.fechaIngreso() != null ? request.fechaIngreso() : LocalDate.now();
        LocalDate fechaVencimiento = request.fechaVencimiento();

        if (fechaVencimiento == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de vencimiento es obligatoria");
        }

        if (!fechaVencimiento.isAfter(fechaIngreso)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de vencimiento debe ser posterior a la fecha de ingreso");
        }

        if (loteRepository.existsByMedicamentoIdAndNumeroLoteIgnoreCase(medicamento.getId(), numeroLote)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe ese numero de lote para el medicamento seleccionado");
        }

        InvLoteMedicamento lote = new InvLoteMedicamento();
        lote.setMedicamento(medicamento);
        lote.setProveedor(proveedor);
        lote.setNumeroLote(numeroLote);
        lote.setFechaIngreso(fechaIngreso);
        lote.setFechaVencimiento(fechaVencimiento);
        lote.setDocumentoIngreso(normalizeOptional(request.documentoIngreso()));
        lote.setGuiaRemision(normalizeOptional(request.guiaRemision()));
        lote.setUbicacionFisica(normalizeOptional(request.ubicacionFisica()));
        lote.setCostoUnitario(costoUnitario);
        lote.setActivo(true);

        InvLoteMedicamento loteGuardado = loteRepository.save(lote);

        InvInventarioLote inventario = new InvInventarioLote();
        inventario.setLote(loteGuardado);
        inventario.setStockActual(cantidad);
        inventario.setStockComprometido(BigDecimal.ZERO);
        inventario.setStockMinimo(stockMinimo);
        inventario.setActivo(true);

        InvInventarioLote inventarioGuardado = inventarioLoteRepository.save(inventario);

        InvKardexMovimiento kardex = new InvKardexMovimiento();
        kardex.setMedicamento(medicamento);
        kardex.setLote(loteGuardado);
        kardex.setTipoMovimiento(MOVIMIENTO_INGRESO_PROVEEDOR);
        kardex.setCantidad(cantidad);
        kardex.setStockAnterior(BigDecimal.ZERO);
        kardex.setStockPosterior(cantidad);
        kardex.setReferenciaTipo("LOTE_INGRESO");
        kardex.setReferenciaId(loteGuardado.getId());
        kardex.setMotivo(defaultText(request.motivo(), "Ingreso inicial de lote por proveedor"));
        kardex.setUsuarioResponsable(defaultText(request.usuarioResponsable(), "usuario_logistica_local"));

        InvKardexMovimiento kardexGuardado = kardexRepository.save(kardex);

        return new StockEntryResponseDto(
                medicamento.getId(),
                medicamento.getNombreComercial(),
                loteGuardado.getId(),
                loteGuardado.getNumeroLote(),
                loteGuardado.getFechaIngreso(),
                loteGuardado.getFechaVencimiento(),
                inventarioGuardado.getId(),
                inventarioGuardado.getStockActual(),
                inventarioGuardado.getStockComprometido(),
                inventarioGuardado.getStockActual().subtract(inventarioGuardado.getStockComprometido()),
                inventarioGuardado.getStockMinimo(),
                kardexGuardado.getId(),
                kardexGuardado.getTipoMovimiento(),
                kardexGuardado.getCantidad(),
                kardexGuardado.getStockAnterior(),
                kardexGuardado.getStockPosterior(),
                kardexGuardado.getFechaMovimiento()
        );
    }

    private String normalizeRequired(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return value.trim();
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

    private BigDecimal positiveRequired(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return value;
    }

    private BigDecimal moneyOrZero(BigDecimal value, String message) {
        BigDecimal result = value != null ? value : BigDecimal.ZERO;
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return result;
    }
}