package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.MedicineRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.MedicineResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.*;
import com.sanfernando.erpclinica.modules.inventory.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MedicineService {

    private final InvMedicamentoComercialRepository medicineRepository;
    private final InvDciRepository dciRepository;
    private final InvCategoriaMedicamentoRepository categoriaRepository;
    private final InvLaboratorioFabricanteRepository laboratorioRepository;
    private final InvUnidadMedidaRepository unidadMedidaRepository;

    public MedicineService(
            InvMedicamentoComercialRepository medicineRepository,
            InvDciRepository dciRepository,
            InvCategoriaMedicamentoRepository categoriaRepository,
            InvLaboratorioFabricanteRepository laboratorioRepository,
            InvUnidadMedidaRepository unidadMedidaRepository
    ) {
        this.medicineRepository = medicineRepository;
        this.dciRepository = dciRepository;
        this.categoriaRepository = categoriaRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @Transactional
    public MedicineResponseDto create(MedicineRequestDto request) {
        String registroSanitario = normalizeRequired(request.registroSanitario(), "El Registro Sanitario es obligatorio");

        if (medicineRepository.existsByRegistroSanitarioIgnoreCase(registroSanitario)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un medicamento con ese Registro Sanitario");
        }

        InvMedicamentoComercial entity = new InvMedicamentoComercial();
        applyRequest(entity, request, registroSanitario);
        entity.setActivo(true);

        return toResponse(medicineRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<MedicineResponseDto> findAllActive() {
        return medicineRepository.findByActivoTrueOrderByNombreComercialAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicineResponseDto findById(Long id) {
        InvMedicamentoComercial entity = findActiveEntity(id);
        return toResponse(entity);
    }

    @Transactional
    public MedicineResponseDto update(Long id, MedicineRequestDto request) {
        InvMedicamentoComercial entity = findActiveEntity(id);
        String registroSanitario = normalizeRequired(request.registroSanitario(), "El Registro Sanitario es obligatorio");

        if (medicineRepository.existsByRegistroSanitarioIgnoreCaseAndIdNot(registroSanitario, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro medicamento con ese Registro Sanitario");
        }

        applyRequest(entity, request, registroSanitario);
        return toResponse(medicineRepository.save(entity));
    }

    @Transactional
    public void deleteLogical(Long id) {
        InvMedicamentoComercial entity = findActiveEntity(id);
        entity.setActivo(false);
        medicineRepository.save(entity);
    }

    private InvMedicamentoComercial findActiveEntity(Long id) {
        return medicineRepository.findById(id)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento comercial no encontrado"));
    }

    private void applyRequest(InvMedicamentoComercial entity, MedicineRequestDto request, String registroSanitario) {
        InvDci dci = dciRepository.findById(request.dciId())
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DCI no encontrado o inactivo"));

        InvCategoriaMedicamento categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria no encontrada o inactiva"));
        }

        InvLaboratorioFabricante laboratorio = null;
        if (request.laboratorioId() != null) {
            laboratorio = laboratorioRepository.findById(request.laboratorioId())
                    .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Laboratorio no encontrado o inactivo"));
        }

        InvUnidadMedida unidadMedida = null;
        if (request.unidadMedidaId() != null) {
            unidadMedida = unidadMedidaRepository.findById(request.unidadMedidaId())
                    .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unidad de medida no encontrada o inactiva"));
        }

        entity.setDci(dci);
        entity.setCategoria(categoria);
        entity.setLaboratorio(laboratorio);
        entity.setUnidadMedida(unidadMedida);
        entity.setNombreComercial(normalizeRequired(request.nombreComercial(), "El nombre comercial es obligatorio"));
        entity.setConcentracion(normalizeRequired(request.concentracion(), "La concentracion es obligatoria"));
        entity.setFormaFarmaceutica(normalizeRequired(request.formaFarmaceutica(), "La forma farmaceutica es obligatoria"));
        entity.setPresentacionComercial(normalizeRequired(request.presentacionComercial(), "La presentacion comercial es obligatoria"));
        entity.setRegistroSanitario(registroSanitario);
        entity.setEsGenerico(defaultBoolean(request.esGenerico()));
        entity.setRequiereReceta(defaultBoolean(request.requiereReceta()));
        entity.setRequiereRecetaArchivada(defaultBoolean(request.requiereRecetaArchivada()));
        entity.setPrecioPublico(defaultMoney(request.precioPublico(), "El precio publico no puede ser negativo"));
        entity.setPrecioAseguradora(defaultMoney(request.precioAseguradora(), "El precio aseguradora no puede ser negativo"));
        entity.setStockMinimoTotal(defaultMoney(request.stockMinimoTotal(), "El stock minimo total no puede ser negativo"));
        entity.setObservaciones(normalizeOptional(request.observaciones()));
    }

    private MedicineResponseDto toResponse(InvMedicamentoComercial entity) {
        InvDci dci = entity.getDci();
        InvCategoriaMedicamento categoria = entity.getCategoria();
        InvLaboratorioFabricante laboratorio = entity.getLaboratorio();
        InvUnidadMedida unidadMedida = entity.getUnidadMedida();

        return new MedicineResponseDto(
                entity.getId(),
                dci != null ? dci.getId() : null,
                dci != null ? dci.getNombreGenerico() : null,
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNombre() : null,
                laboratorio != null ? laboratorio.getId() : null,
                laboratorio != null ? laboratorio.getNombre() : null,
                unidadMedida != null ? unidadMedida.getId() : null,
                unidadMedida != null ? unidadMedida.getCodigo() : null,
                unidadMedida != null ? unidadMedida.getNombre() : null,
                entity.getNombreComercial(),
                entity.getConcentracion(),
                entity.getFormaFarmaceutica(),
                entity.getPresentacionComercial(),
                entity.getRegistroSanitario(),
                entity.getEsGenerico(),
                entity.getRequiereReceta(),
                entity.getRequiereRecetaArchivada(),
                entity.getPrecioPublico(),
                entity.getPrecioAseguradora(),
                entity.getStockMinimoTotal(),
                entity.getObservaciones(),
                entity.getActivo(),
                entity.getCreadoEn(),
                entity.getActualizadoEn()
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

    private Boolean defaultBoolean(Boolean value) {
        return value != null ? value : false;
    }

    private BigDecimal defaultMoney(BigDecimal value, String errorMessage) {
        BigDecimal result = value != null ? value : BigDecimal.ZERO;
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return result;
    }
}