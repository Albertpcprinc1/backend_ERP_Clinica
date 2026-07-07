package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.KardexMovementResponseDto;
import com.sanfernando.erpclinica.modules.inventory.entity.InvDci;
import com.sanfernando.erpclinica.modules.inventory.entity.InvKardexMovimiento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvLoteMedicamento;
import com.sanfernando.erpclinica.modules.inventory.entity.InvMedicamentoComercial;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KardexQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<KardexMovementResponseDto> findByFilters(
            Long medicamentoId,
            Long loteId,
            String tipoMovimiento,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InvKardexMovimiento> query = cb.createQuery(InvKardexMovimiento.class);
        Root<InvKardexMovimiento> root = query.from(InvKardexMovimiento.class);

        Fetch<InvKardexMovimiento, InvMedicamentoComercial> medicamentoFetch =
                root.fetch("medicamento", JoinType.INNER);
        medicamentoFetch.fetch("dci", JoinType.LEFT);
        root.fetch("lote", JoinType.INNER);

        Join<InvKardexMovimiento, InvMedicamentoComercial> medicamentoJoin =
                root.join("medicamento", JoinType.INNER);
        Join<InvKardexMovimiento, InvLoteMedicamento> loteJoin =
                root.join("lote", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        if (medicamentoId != null) {
            predicates.add(cb.equal(medicamentoJoin.get("id"), medicamentoId));
        }

        if (loteId != null) {
            predicates.add(cb.equal(loteJoin.get("id"), loteId));
        }

        String tipoMovimientoNormalizado = normalizeOptional(tipoMovimiento);
        if (tipoMovimientoNormalizado != null) {
            predicates.add(cb.equal(root.get("tipoMovimiento"), tipoMovimientoNormalizado));
        }

        if (fechaDesde != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fechaMovimiento"), fechaDesde));
        }

        if (fechaHasta != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fechaMovimiento"), fechaHasta));
        }

        query.select(root).distinct(true);

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.orderBy(
                cb.desc(root.get("fechaMovimiento")),
                cb.desc(root.get("id"))
        );

        return entityManager.createQuery(query)
                .getResultList()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private KardexMovementResponseDto toResponse(InvKardexMovimiento entity) {
        InvMedicamentoComercial medicamento = entity.getMedicamento();
        InvDci dci = medicamento != null ? medicamento.getDci() : null;
        InvLoteMedicamento lote = entity.getLote();

        return new KardexMovementResponseDto(
                entity.getId(),
                medicamento != null ? medicamento.getId() : null,
                medicamento != null ? medicamento.getNombreComercial() : null,
                dci != null ? dci.getNombreGenerico() : null,
                lote != null ? lote.getId() : null,
                lote != null ? lote.getNumeroLote() : null,
                lote != null ? lote.getFechaVencimiento() : null,
                entity.getTipoMovimiento(),
                entity.getCantidad(),
                entity.getStockAnterior(),
                entity.getStockPosterior(),
                entity.getReferenciaTipo(),
                entity.getReferenciaId(),
                entity.getMotivo(),
                entity.getUsuarioResponsable(),
                entity.getFechaMovimiento()
        );
    }

    private String normalizeOptional(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}