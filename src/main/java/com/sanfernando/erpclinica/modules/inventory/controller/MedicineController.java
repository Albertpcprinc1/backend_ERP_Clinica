package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.MedicineRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.MedicineResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/medicines")
public class MedicineController {

    private final MedicineService service;

    public MedicineController(MedicineService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicineResponseDto create(@Valid @RequestBody MedicineRequestDto request) {
        return service.create(request);
    }

    @GetMapping
    public List<MedicineResponseDto> findAllActive() {
        return service.findAllActive();
    }

    @GetMapping("/{id}")
    public MedicineResponseDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public MedicineResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody MedicineRequestDto request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLogical(@PathVariable Long id) {
        service.deleteLogical(id);
    }
}