package com.sanfernando.erpclinica.modules.inventory.controller;

import com.sanfernando.erpclinica.modules.inventory.dto.KardexMovementResponseDto;
import com.sanfernando.erpclinica.modules.inventory.service.KardexQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/kardex")
public class KardexQueryController {

    private final KardexQueryService service;

    public KardexQueryController(KardexQueryService service) {
        this.service = service;
    }

    @GetMapping
    public List<KardexMovementResponseDto> findByFilters(
            @RequestParam(required = false) Long medicamentoId,
            @RequestParam(required = false) Long loteId,
            @RequestParam(required = false) String tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
    ) {
        return service.findByFilters(medicamentoId, loteId, tipoMovimiento, fechaDesde, fechaHasta);
    }
}