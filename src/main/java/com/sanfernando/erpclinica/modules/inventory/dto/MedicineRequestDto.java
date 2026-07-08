package com.sanfernando.erpclinica.modules.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MedicineRequestDto(

        @NotNull(message = "El DCI es obligatorio")
        Long dciId,

        Long categoriaId,

        Long laboratorioId,

        Long unidadMedidaId,

        @NotBlank(message = "El nombre comercial es obligatorio")
        @Size(max = 180, message = "El nombre comercial no debe superar 180 caracteres")
        String nombreComercial,

        @NotBlank(message = "La concentracion es obligatoria")
        @Size(max = 120, message = "La concentracion no debe superar 120 caracteres")
        String concentracion,

        @NotBlank(message = "La forma farmaceutica es obligatoria")
        @Size(max = 120, message = "La forma farmaceutica no debe superar 120 caracteres")
        String formaFarmaceutica,

        @NotBlank(message = "La presentacion comercial es obligatoria")
        @Size(max = 220, message = "La presentacion comercial no debe superar 220 caracteres")
        String presentacionComercial,

        @NotBlank(message = "La unidad de presentacion es obligatoria")
        @Size(max = 80, message = "La unidad de presentacion no debe superar 80 caracteres")
        String unidadPresentacion,

        @NotNull(message = "El factor de conversion a unidad base es obligatorio")
        @DecimalMin(value = "0.01", message = "El factor de conversion debe ser mayor a cero")
        BigDecimal factorConversionUnidadBase,

        @NotBlank(message = "El Registro Sanitario es obligatorio")
        @Size(max = 80, message = "El Registro Sanitario no debe superar 80 caracteres")
        String registroSanitario,

        Boolean esGenerico,

        Boolean requiereReceta,

        Boolean requiereRecetaArchivada,

        @DecimalMin(value = "0.00", message = "El precio publico no puede ser negativo")
        BigDecimal precioPublico,

        @DecimalMin(value = "0.00", message = "El precio aseguradora no puede ser negativo")
        BigDecimal precioAseguradora,

        @DecimalMin(value = "0.00", message = "El stock minimo total no puede ser negativo")
        BigDecimal stockMinimoTotal,

        @Size(max = 600, message = "Las observaciones no deben superar 600 caracteres")
        String observaciones
) {
}