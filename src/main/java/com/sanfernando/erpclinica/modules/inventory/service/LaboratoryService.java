package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.LaboratoryRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.LaboratoryResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLaboratorioFabricante;
import com.sanfernando.erpclinica.modules.inventory.repository.InvLaboratorioFabricanteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LaboratoryService {

    private final InvLaboratorioFabricanteRepository repository;

    public LaboratoryService(InvLaboratorioFabricanteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public LaboratoryResponseDto create(LaboratoryRequestDto request) {
        String nombre = normalizeRequired(request.nombre(), "El nombre del laboratorio es obligatorio");

        if (repository.existsByNombreIgnoreCase(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un laboratorio con ese nombre");
        }

        InvLaboratorioFabricante entity = new InvLaboratorioFabricante();
        applyRequest(entity, request, nombre);
        entity.setActivo(true);

        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<LaboratoryResponseDto> findAllActive() {
        return repository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LaboratoryResponseDto findById(Long id) {
        InvLaboratorioFabricante entity = findActiveEntity(id);
        return toResponse(entity);
    }

    @Transactional
    public LaboratoryResponseDto update(Long id, LaboratoryRequestDto request) {
        InvLaboratorioFabricante entity = findActiveEntity(id);
        String nombre = normalizeRequired(request.nombre(), "El nombre del laboratorio es obligatorio");

        if (repository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro laboratorio con ese nombre");
        }

        applyRequest(entity, request, nombre);
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void deleteLogical(Long id) {
        InvLaboratorioFabricante entity = findActiveEntity(id);
        entity.setActivo(false);
        repository.save(entity);
    }

    private InvLaboratorioFabricante findActiveEntity(Long id) {
        return repository.findById(id)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Laboratorio no encontrado"));
    }

    private void applyRequest(InvLaboratorioFabricante entity, LaboratoryRequestDto request, String nombreNormalizado) {
        entity.setNombre(nombreNormalizado);
        entity.setRuc(normalizeOptional(request.ruc()));
        entity.setPaisOrigen(normalizeOptional(request.paisOrigen()));
        entity.setTelefono(normalizeOptional(request.telefono()));
        entity.setEmail(normalizeOptional(request.email()));
        entity.setDireccion(normalizeOptional(request.direccion()));
    }

    private LaboratoryResponseDto toResponse(InvLaboratorioFabricante entity) {
        return new LaboratoryResponseDto(
                entity.getId(),
                entity.getNombre(),
                entity.getRuc(),
                entity.getPaisOrigen(),
                entity.getTelefono(),
                entity.getEmail(),
                entity.getDireccion(),
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
}