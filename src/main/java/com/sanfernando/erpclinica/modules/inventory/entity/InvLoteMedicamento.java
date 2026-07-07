package com.sanfernando.erpclinica.modules.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inv_lote_medicamento")
public class InvLoteMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private InvMedicamentoComercial medicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private InvProveedor proveedor;

    @Column(name = "numero_lote", nullable = false, length = 100)
    private String numeroLote;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "documento_ingreso", length = 120)
    private String documentoIngreso;

    @Column(name = "guia_remision", length = 120)
    private String guiaRemision;

    @Column(name = "ubicacion_fisica", length = 160)
    private String ubicacionFisica;

    @Column(name = "costo_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoUnitario = BigDecimal.ZERO;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    public InvLoteMedicamento() {
    }

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        if (this.fechaIngreso == null) this.fechaIngreso = LocalDate.now();
        if (this.activo == null) this.activo = true;
        if (this.costoUnitario == null) this.costoUnitario = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public InvMedicamentoComercial getMedicamento() {
        return medicamento;
    }

    public InvProveedor getProveedor() {
        return proveedor;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getDocumentoIngreso() {
        return documentoIngreso;
    }

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public String getUbicacionFisica() {
        return ubicacionFisica;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
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

    public void setMedicamento(InvMedicamentoComercial medicamento) {
        this.medicamento = medicamento;
    }

    public void setProveedor(InvProveedor proveedor) {
        this.proveedor = proveedor;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setDocumentoIngreso(String documentoIngreso) {
        this.documentoIngreso = documentoIngreso;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public void setUbicacionFisica(String ubicacionFisica) {
        this.ubicacionFisica = ubicacionFisica;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}