package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvDci;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvDciRepository extends JpaRepository<InvDci, Long> {

    List<InvDci> findByActivoTrueOrderByNombreGenericoAsc();

    Optional<InvDci> findByNombreGenericoIgnoreCase(String nombreGenerico);

    boolean existsByNombreGenericoIgnoreCase(String nombreGenerico);

    boolean existsByNombreGenericoIgnoreCaseAndIdNot(String nombreGenerico, Long id);
}