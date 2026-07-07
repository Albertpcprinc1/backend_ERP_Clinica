package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.StockEntryRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.StockEntryResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.StockEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/stock-entries")
public class StockEntryController {

    private final StockEntryService service;

    public StockEntryController(StockEntryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockEntryResponseDto registerStockEntry(@Valid @RequestBody StockEntryRequestDto request) {
        return service.registerStockEntry(request);
    }
}