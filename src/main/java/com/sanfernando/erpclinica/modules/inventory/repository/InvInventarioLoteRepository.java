package com.sanfernando.erpclinica.modules.inventory.repository;

import com.sanfernando.erpclinica.modules.inventory.entity.InvInventarioLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvInventarioLoteRepository extends JpaRepository<InvInventarioLote, Long> {

    List<InvInventarioLote> findByActivoTrueOrderByIdAsc();

    List<InvInventarioLote> findByActivoTrueOrderByLoteFechaVencimientoAsc();

    Optional<InvInventarioLote> findByLoteId(Long loteId);

    @Query("""
            SELECT i
            FROM InvInventarioLote i
            JOIN FETCH i.lote l
            JOIN FETCH l.medicamento m
            LEFT JOIN FETCH m.dci d
            LEFT JOIN FETCH l.proveedor p
            WHERE i.activo = true
              AND l.activo = true
              AND m.activo = true
              AND m.id = :medicamentoId
              AND l.fechaVencimiento >= CURRENT_DATE
              AND i.stockActual > i.stockComprometido
            ORDER BY l.fechaVencimiento ASC, l.id ASC
            """)
    List<InvInventarioLote> findAvailableLotsByMedicineFefo(@Param("medicamentoId") Long medicamentoId);
}