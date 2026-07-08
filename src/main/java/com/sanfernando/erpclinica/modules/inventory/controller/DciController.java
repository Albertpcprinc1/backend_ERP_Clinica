package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.DciRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.DciResponseDto;
import com.sanfernando.erpclinica.modules.inventory.dto.DciStockSummaryDto;
import com.sanfernando.erpclinica.modules.inventory.service.DciService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/dci")
public class DciController {

    private final DciService dciService;

    public DciController(DciService dciService) {
        this.dciService = dciService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DciResponseDto create(@Valid @RequestBody DciRequestDto request) {
        return dciService.create(request);
    }

    @GetMapping
    public List<DciResponseDto> findAllActive() {
        return dciService.findAllActive();
    }

    @GetMapping("/stock-summary")
    public List<DciStockSummaryDto> findStockSummary() {
        return dciService.findStockSummary();
    }

    @GetMapping("/{id}")
    public DciResponseDto findById(@PathVariable Long id) {
        return dciService.findById(id);
    }

    @PutMapping("/{id}")
    public DciResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody DciRequestDto request
    ) {
        return dciService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLogical(@PathVariable Long id) {
        dciService.deleteLogical(id);
    }
}