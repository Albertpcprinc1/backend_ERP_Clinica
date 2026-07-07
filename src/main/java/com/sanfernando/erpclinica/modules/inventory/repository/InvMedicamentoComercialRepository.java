package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvMedicamentoComercial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvMedicamentoComercialRepository extends JpaRepository<InvMedicamentoComercial, Long> {

    List<InvMedicamentoComercial> findByActivoTrueOrderByNombreComercialAsc();

    boolean existsByRegistroSanitarioIgnoreCase(String registroSanitario);

    boolean existsByRegistroSanitarioIgnoreCaseAndIdNot(String registroSanitario, Long id);
}