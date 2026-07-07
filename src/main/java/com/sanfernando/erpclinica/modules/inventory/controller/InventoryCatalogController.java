package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.InventoryCatalogsResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.InventoryCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory/catalogs")
public class InventoryCatalogController {

    private final InventoryCatalogService inventoryCatalogService;

    public InventoryCatalogController(InventoryCatalogService inventoryCatalogService) {
        this.inventoryCatalogService = inventoryCatalogService;
    }

    @GetMapping
    public InventoryCatalogsResponseDto getCatalogs() {
        return inventoryCatalogService.getCatalogs();
    }
}