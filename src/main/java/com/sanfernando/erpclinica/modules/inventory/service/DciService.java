package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.DciRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.DciResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvDci;
import com.sanfernando.erpclinica.modules.inventory.repository.InvDciRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DciService {

    private final InvDciRepository dciRepository;

    public DciService(InvDciRepository dciRepository) {
        this.dciRepository = dciRepository;
    }

    @Transactional
    public DciResponseDto create(DciRequestDto request) {
        String nombre = normalizeRequired(request.nombreGenerico());

        if (dciRepository.existsByNombreGenericoIgnoreCase(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un DCI con ese nombre generico");
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
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}