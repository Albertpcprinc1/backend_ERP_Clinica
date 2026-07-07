package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.ProviderRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.ProviderResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvProveedor;
import com.sanfernando.erpclinica.modules.inventory.repository.InvProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProviderService {

    private final InvProveedorRepository repository;

    public ProviderService(InvProveedorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProviderResponseDto create(ProviderRequestDto request) {
        String razonSocial = normalizeRequired(request.razonSocial(), "La razon social es obligatoria");
        String ruc = normalizeOptional(request.ruc());

        if (ruc != null && repository.existsByRucIgnoreCase(ruc)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un proveedor con ese RUC");
        }

        InvProveedor entity = new InvProveedor();
        applyRequest(entity, request, razonSocial, ruc);
        entity.setActivo(true);

        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<ProviderResponseDto> findAllActive() {
        return repository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProviderResponseDto findById(Long id) {
        InvProveedor entity = findActiveEntity(id);
        return toResponse(entity);
    }

    @Transactional
    public ProviderResponseDto update(Long id, ProviderRequestDto request) {
        InvProveedor entity = findActiveEntity(id);
        String razonSocial = normalizeRequired(request.razonSocial(), "La razon social es obligatoria");
        String ruc = normalizeOptional(request.ruc());

        if (ruc != null && repository.existsByRucIgnoreCaseAndIdNot(ruc, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro proveedor con ese RUC");
        }

        applyRequest(entity, request, razonSocial, ruc);
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void deleteLogical(Long id) {
        InvProveedor entity = findActiveEntity(id);
        entity.setActivo(false);
        repository.save(entity);
    }

    private InvProveedor findActiveEntity(Long id) {
        return repository.findById(id)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
    }

    private void applyRequest(InvProveedor entity, ProviderRequestDto request, String razonSocial, String ruc) {
        entity.setRazonSocial(razonSocial);
        entity.setRuc(ruc);
        entity.setTelefono(normalizeOptional(request.telefono()));
        entity.setEmail(normalizeOptional(request.email()));
        entity.setDireccion(normalizeOptional(request.direccion()));
        entity.setContacto(normalizeOptional(request.contacto()));
    }

    private ProviderResponseDto toResponse(InvProveedor entity) {
        return new ProviderResponseDto(
                entity.getId(),
                entity.getRazonSocial(),
                entity.getRuc(),
                entity.getTelefono(),
                entity.getEmail(),
                entity.getDireccion(),
                entity.getContacto(),
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