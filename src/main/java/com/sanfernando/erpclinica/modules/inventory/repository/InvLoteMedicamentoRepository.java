package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvLoteMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvLoteMedicamentoRepository extends JpaRepository<InvLoteMedicamento, Long> {

    List<InvLoteMedicamento> findByActivoTrueOrderByFechaVencimientoAsc();

    boolean existsByMedicamentoIdAndNumeroLoteIgnoreCase(Long medicamentoId, String numeroLote);
}