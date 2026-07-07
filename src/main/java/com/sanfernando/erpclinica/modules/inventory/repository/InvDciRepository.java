package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvDci;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvDciRepository extends JpaRepository<InvDci, Long> {

    List<InvDci> findByActivoTrueOrderByNombreGenericoAsc();

    boolean existsByNombreGenericoIgnoreCase(String nombreGenerico);

    boolean existsByNombreGenericoIgnoreCaseAndIdNot(String nombreGenerico, Long id);
}