package com.sanfernando.erpclinica.modules.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inv_medicamento_comercial")
public class InvMedicamentoComercial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dci_id", nullable = false)
    private InvDci dci;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private InvCategoriaMedicamento categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id")
    private InvLaboratorioFabricante laboratorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_medida_id")
    private InvUnidadMedida unidadMedida;

    @Column(name = "nombre_comercial", nullable = false, length = 180)
    private String nombreComercial;

    @Column(name = "concentracion", nullable = false, length = 120)
    private String concentracion;

    @Column(name = "forma_farmaceutica", nullable = false, length = 120)
    private String formaFarmaceutica;

    @Column(name = "presentacion_comercial", nullable = false, length = 220)
    private String presentacionComercial;

    @Column(name = "unidad_presentacion", nullable = false, length = 80)
    private String unidadPresentacion = "Unidad";

    @Column(name = "factor_conversion_unidad_base", nullable = false, precision = 14, scale = 2)
    private BigDecimal factorConversionUnidadBase = BigDecimal.ONE;

    @Column(name = "registro_sanitario", nullable = false, length = 80)
    private String registroSanitario;

    @Column(name = "es_generico", nullable = false)
    private Boolean esGenerico = false;

    @Column(name = "requiere_receta", nullable = false)
    private Boolean requiereReceta = false;

    @Column(name = "requiere_receta_archivada", nullable = false)
    private Boolean requiereRecetaArchivada = false;

    @Column(name = "precio_publico", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioPublico = BigDecimal.ZERO;

    @Column(name = "precio_aseguradora", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioAseguradora = BigDecimal.ZERO;

    @Column(name = "stock_minimo_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockMinimoTotal = BigDecimal.ZERO;

    @Column(name = "observaciones", length = 600)
    private String observaciones;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    public InvMedicamentoComercial() {
    }

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.esGenerico == null) this.esGenerico = false;
        if (this.requiereReceta == null) this.requiereReceta = false;
        if (this.requiereRecetaArchivada == null) this.requiereRecetaArchivada = false;
        if (this.precioPublico == null) this.precioPublico = BigDecimal.ZERO;
        if (this.precioAseguradora == null) this.precioAseguradora = BigDecimal.ZERO;
        if (this.stockMinimoTotal == null) this.stockMinimoTotal = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public InvDci getDci() {
        return dci;
    }

    public InvCategoriaMedicamento getCategoria() {
        return categoria;
    }

    public InvLaboratorioFabricante getLaboratorio() {
        return laboratorio;
    }

    public InvUnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public String getPresentacionComercial() {
        return presentacionComercial;
    }

    public String getRegistroSanitario() {
        return registroSanitario;
    }

    public Boolean getEsGenerico() {
        return esGenerico;
    }

    public Boolean getRequiereReceta() {
        return requiereReceta;
    }

    public Boolean getRequiereRecetaArchivada() {
        return requiereRecetaArchivada;
    }

    public BigDecimal getPrecioPublico() {
        return precioPublico;
    }

    public BigDecimal getPrecioAseguradora() {
        return precioAseguradora;
    }

    public BigDecimal getStockMinimoTotal() {
        return stockMinimoTotal;
    }

    public String getObservaciones() {
        return observaciones;
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

    public void setDci(InvDci dci) {
        this.dci = dci;
    }

    public void setCategoria(InvCategoriaMedicamento categoria) {
        this.categoria = categoria;
    }

    public void setLaboratorio(InvLaboratorioFabricante laboratorio) {
        this.laboratorio = laboratorio;
    }

    public void setUnidadMedida(InvUnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public void setPresentacionComercial(String presentacionComercial) {
        this.presentacionComercial = presentacionComercial;
    }

    public void setRegistroSanitario(String registroSanitario) {
        this.registroSanitario = registroSanitario;
    }

    public void setEsGenerico(Boolean esGenerico) {
        this.esGenerico = esGenerico;
    }

    public void setRequiereReceta(Boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }

    public void setRequiereRecetaArchivada(Boolean requiereRecetaArchivada) {
        this.requiereRecetaArchivada = requiereRecetaArchivada;
    }

    public void setPrecioPublico(BigDecimal precioPublico) {
        this.precioPublico = precioPublico;
    }

    public void setPrecioAseguradora(BigDecimal precioAseguradora) {
        this.precioAseguradora = precioAseguradora;
    }

    public void setStockMinimoTotal(BigDecimal stockMinimoTotal) {
        this.stockMinimoTotal = stockMinimoTotal;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUnidadPresentacion() {
        return unidadPresentacion;
    }

    public void setUnidadPresentacion(String unidadPresentacion) {
        this.unidadPresentacion = unidadPresentacion;
    }

    public BigDecimal getFactorConversionUnidadBase() {
        return factorConversionUnidadBase;
    }

    public void setFactorConversionUnidadBase(BigDecimal factorConversionUnidadBase) {
        this.factorConversionUnidadBase = factorConversionUnidadBase;
    }}