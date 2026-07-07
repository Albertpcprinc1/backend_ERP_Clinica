package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvCategoriaMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvCategoriaMedicamentoRepository extends JpaRepository<InvCategoriaMedicamento, Long> {
    List<InvCategoriaMedicamento> findByActivoTrueOrderByNombreAsc();
}