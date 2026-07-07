package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvUnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvUnidadMedidaRepository extends JpaRepository<InvUnidadMedida, Long> {
    List<InvUnidadMedida> findByActivoTrueOrderByCodigoAsc();
}