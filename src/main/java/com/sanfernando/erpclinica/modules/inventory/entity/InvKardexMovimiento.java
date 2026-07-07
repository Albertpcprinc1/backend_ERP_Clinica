package com.sanfernando.erpclinica.modules.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inv_kardex_movimiento")
public class InvKardexMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private InvMedicamentoComercial medicamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private InvLoteMedicamento lote;

    @Column(name = "tipo_movimiento", nullable = false, length = 60)
    private String tipoMovimiento;

    @Column(name = "cantidad", nullable = false, precision = 14, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "stock_anterior", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockAnterior;

    @Column(name = "stock_posterior", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockPosterior;

    @Column(name = "referencia_tipo", length = 80)
    private String referenciaTipo;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(name = "motivo", length = 600)
    private String motivo;

    @Column(name = "usuario_responsable", length = 160)
    private String usuarioResponsable;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    public InvKardexMovimiento() {
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaMovimiento == null) {
            this.fechaMovimiento = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public InvMedicamentoComercial getMedicamento() {
        return medicamento;
    }

    public InvLoteMedicamento getLote() {
        return lote;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getStockAnterior() {
        return stockAnterior;
    }

    public BigDecimal getStockPosterior() {
        return stockPosterior;
    }

    public String getReferenciaTipo() {
        return referenciaTipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setMedicamento(InvMedicamentoComercial medicamento) {
        this.medicamento = medicamento;
    }

    public void setLote(InvLoteMedicamento lote) {
        this.lote = lote;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public void setStockAnterior(BigDecimal stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public void setStockPosterior(BigDecimal stockPosterior) {
        this.stockPosterior = stockPosterior;
    }

    public void setReferenciaTipo(String referenciaTipo) {
        this.referenciaTipo = referenciaTipo;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}