package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.InventoryDashboardSummaryDto;
import com.sanfernando.erpclinica.modules.inventory.service.InventoryDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryDashboardController {

    private final InventoryDashboardService service;

    public InventoryDashboardController(InventoryDashboardService service) {
        this.service = service;
    }

    @GetMapping("/api/inventory/dashboard-summary")
    public InventoryDashboardSummaryDto getSummary() {
        return service.getSummary();
    }
}