package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.StockOutputRequestDto;
import com.sanfernando.erpclinica.modules.inventory.dto.StockOutputResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.StockOutputService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/stock-outputs")
public class StockOutputController {

    private final StockOutputService service;

    public StockOutputController(StockOutputService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockOutputResponseDto registerStockOutput(@Valid @RequestBody StockOutputRequestDto request) {
        return service.registerStockOutput(request);
    }
}