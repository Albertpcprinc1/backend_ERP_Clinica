package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.DciRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.DciResponseDto;
import com.sanfernando.erpclinica.modules.inventory.dto.DciStockSummaryDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvDci;
import com.sanfernando.erpclinica.modules.inventory.entity.InvInventarioLote;
import com.sanfernando.erpclinica.modules.inventory.entity.InvMedicamentoComercial;
import com.sanfernando.erpclinica.modules.inventory.repository.InvDciRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvInventarioLoteRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvMedicamentoComercialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DciService {

    private final InvDciRepository dciRepository;
    private final InvMedicamentoComercialRepository medicamentoRepository;
    private final InvInventarioLoteRepository inventarioLoteRepository;

    public DciService(
            InvDciRepository dciRepository,
            InvMedicamentoComercialRepository medicamentoRepository,
            InvInventarioLoteRepository inventarioLoteRepository
    ) {
        this.dciRepository = dciRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.inventarioLoteRepository = inventarioLoteRepository;
    }

    @Transactional
    public DciResponseDto create(DciRequestDto request) {
        String nombre = normalizeRequired(request.nombreGenerico());

        InvDci existing = dciRepository.findByNombreGenericoIgnoreCase(nombre).orElse(null);

        if (existing != null && Boolean.TRUE.equals(existing.getActivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un DCI activo con ese nombre generico");
        }

        if (existing != null) {
            existing.setDescripcion(normalizeOptional(request.descripcion()));
            existing.setActivo(true);
            return toResponse(dciRepository.save(existing));
        }

        InvDci entity = new InvDci();
        entity.setNombreGenerico(nombre);
        entity.setDescripcion(normalizeOptional(request.descripcion()));
        entity.setActivo(true);

        return toResponse(dciRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<DciResponseDto> findAllActive() {
        return dciRepository.findByActivoTrueOrderByNombreGenericoAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DciStockSummaryDto> findStockSummary() {
        List<InvDci> dcis = dciRepository.findByActivoTrueOrderByNombreGenericoAsc();
        List<InvMedicamentoComercial> medicamentos = medicamentoRepository.findByActivoTrueOrderByNombreComercialAsc();
        List<InvInventarioLote> inventarios = inventarioLoteRepository.findByActivoTrueOrderByLoteFechaVencimientoAsc();

        return dcis.stream()
                .map(dci -> toStockSummary(dci, medicamentos, inventarios))
                .toList();
    }

    @Transactional(readOnly = true)
    public DciResponseDto findById(Long id) {
        InvDci entity = dciRepository.findById(id)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DCI no encontrado"));

        return toResponse(entity);
    }

    @Transactional
    public DciResponseDto update(Long id, DciRequestDto request) {
        InvDci entity = dciRepository.findById(id)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DCI no encontrado"));

        String nombre = normalizeRequired(request.nombreGenerico());

        if (dciRepository.existsByNombreGenericoIgnoreCaseAndIdNot(nombre, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro DCI con ese nombre generico");
        }

        entity.setNombreGenerico(nombre);
        entity.setDescripcion(normalizeOptional(request.descripcion()));

        return toResponse(dciRepository.save(entity));
    }

    @Transactional
    public void deleteLogical(Long id) {
        InvDci entity = dciRepository.findById(id)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DCI no encontrado"));

        entity.setActivo(false);
        dciRepository.save(entity);
    }

    private DciStockSummaryDto toStockSummary(
            InvDci dci,
            List<InvMedicamentoComercial> medicamentos,
            List<InvInventarioLote> inventarios
    ) {
        List<InvMedicamentoComercial> medicamentosDelDci = medicamentos.stream()
                .filter(medicamento -> medicamento.getDci() != null)
                .filter(medicamento -> dci.getId().equals(medicamento.getDci().getId()))
                .toList();

        Long totalMedicamentosComerciales = (long) medicamentosDelDci.size();

        Long totalLotesActivos = inventarios.stream()
                .filter(inventario -> inventario.getLote() != null)
                .filter(inventario -> inventario.getLote().getMedicamento() != null)
                .filter(inventario -> inventario.getLote().getMedicamento().getDci() != null)
                .filter(inventario -> dci.getId().equals(inventario.getLote().getMedicamento().getDci().getId()))
                .count();

        BigDecimal stockTotalDisponible = inventarios.stream()
                .filter(inventario -> inventario.getLote() != null)
                .filter(inventario -> inventario.getLote().getMedicamento() != null)
                .filter(inventario -> inventario.getLote().getMedicamento().getDci() != null)
                .filter(inventario -> dci.getId().equals(inventario.getLote().getMedicamento().getDci().getId()))
                .map(this::stockDisponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DciStockSummaryDto(
                dci.getId(),
                dci.getNombreGenerico(),
                dci.getDescripcion(),
                totalMedicamentosComerciales,
                totalLotesActivos,
                stockTotalDisponible,
                dci.getActivo()
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

    private DciResponseDto toResponse(InvDci entity) {
        return new DciResponseDto(
                entity.getId(),
                entity.getNombreGenerico(),
                entity.getDescripcion(),
                entity.getActivo(),
                entity.getCreadoEn(),
                entity.getActualizadoEn()
        );
    }

    private String normalizeRequired(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre generico es obligatorio");
        }
        return value.trim();
    }

    private String normalizeOptional(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}