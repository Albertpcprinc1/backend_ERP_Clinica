package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvKardexMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvKardexMovimientoRepository extends JpaRepository<InvKardexMovimiento, Long> {

    List<InvKardexMovimiento> findTop10ByOrderByFechaMovimientoDesc();

    List<InvKardexMovimiento> findTop50ByOrderByFechaMovimientoDesc();
}