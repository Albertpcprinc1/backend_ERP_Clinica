package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.StockLotResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.StockQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/stocks")
public class StockQueryController {

    private final StockQueryService service;

    public StockQueryController(StockQueryService service) {
        this.service = service;
    }

    @GetMapping
    public List<StockLotResponseDto> findAllStocksFefo() {
        return service.findAllStocksFefo();
    }
}