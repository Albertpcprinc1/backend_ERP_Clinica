package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.LaboratoryRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.LaboratoryResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.LaboratoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/laboratories")
public class LaboratoryController {

    private final LaboratoryService service;

    public LaboratoryController(LaboratoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LaboratoryResponseDto create(@Valid @RequestBody LaboratoryRequestDto request) {
        return service.create(request);
    }

    @GetMapping
    public List<LaboratoryResponseDto> findAllActive() {
        return service.findAllActive();
    }

    @GetMapping("/{id}")
    public LaboratoryResponseDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public LaboratoryResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody LaboratoryRequestDto request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLogical(@PathVariable Long id) {
        service.deleteLogical(id);
    }
}