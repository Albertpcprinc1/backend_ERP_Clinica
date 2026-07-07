package com.sanfernando.erpclinica.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/public/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "OK",
                "project", "ERP_CLINICA_PRINCIPAL",
                "client", "Centro Medico San Fernando Huaraz",
                "module", "Backend Base",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}