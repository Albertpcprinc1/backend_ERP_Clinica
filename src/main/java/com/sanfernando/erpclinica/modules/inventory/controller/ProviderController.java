package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.ProviderRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.ProviderResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/providers")
public class ProviderController {

    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderResponseDto create(@Valid @RequestBody ProviderRequestDto request) {
        return service.create(request);
    }

    @GetMapping
    public List<ProviderResponseDto> findAllActive() {
        return service.findAllActive();
    }

    @GetMapping("/{id}")
    public ProviderResponseDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ProviderResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequestDto request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLogical(@PathVariable Long id) {
        service.deleteLogical(id);
    }
}