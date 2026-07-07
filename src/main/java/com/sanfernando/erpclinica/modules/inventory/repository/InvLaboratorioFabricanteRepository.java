package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvLaboratorioFabricante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvLaboratorioFabricanteRepository extends JpaRepository<InvLaboratorioFabricante, Long> {

    List<InvLaboratorioFabricante> findByActivoTrueOrderByNombreAsc();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}