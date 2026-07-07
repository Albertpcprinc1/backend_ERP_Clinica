package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvProveedorRepository extends JpaRepository<InvProveedor, Long> {

    List<InvProveedor> findByActivoTrueOrderByRazonSocialAsc();

    boolean existsByRucIgnoreCase(String ruc);

    boolean existsByRucIgnoreCaseAndIdNot(String ruc, Long id);
}