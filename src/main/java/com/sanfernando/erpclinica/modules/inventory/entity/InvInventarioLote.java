package com.sanfernando.erpclinica.modules.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inv_inventario_lote")
public class InvInventarioLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private InvLoteMedicamento lote;

    @Column(name = "stock_actual", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(name = "stock_comprometido", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockComprometido = BigDecimal.ZERO;

    @Column(name = "stock_minimo", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    @Column(name = "stock_disponible", precision = 14, scale = 2, insertable = false, updatable = false)
    private BigDecimal stockDisponible;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    public InvInventarioLote() {
    }

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.stockActual == null) this.stockActual = BigDecimal.ZERO;
        if (this.stockComprometido == null) this.stockComprometido = BigDecimal.ZERO;
        if (this.stockMinimo == null) this.stockMinimo = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public InvLoteMedicamento getLote() {
        return lote;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public BigDecimal getStockComprometido() {
        return stockComprometido;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public BigDecimal getStockDisponible() {
        return stockDisponible;
    }

    public Boolean getActivo() {
        return activo;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setLote(InvLoteMedicamento lote) {
        this.lote = lote;
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public void setStockComprometido(BigDecimal stockComprometido) {
        this.stockComprometido = stockComprometido;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}