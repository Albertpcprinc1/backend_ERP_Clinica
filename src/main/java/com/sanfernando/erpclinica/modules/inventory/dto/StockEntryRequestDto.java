package com.sanfernando.erpclinica.modules.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockEntryRequestDto(

        @NotNull(message = "El medicamento es obligatorio")
        Long medicamentoId,

        Long proveedorId,

        @NotBlank(message = "El numero de lote es obligatorio")
        @Size(max = 100, message = "El numero de lote no debe superar 100 caracteres")
        String numeroLote,

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        LocalDate fechaVencimiento,

        LocalDate fechaIngreso,

        @Size(max = 120, message = "El documento de ingreso no debe superar 120 caracteres")
        String documentoIngreso,

        @Size(max = 120, message = "La guia de remision no debe superar 120 caracteres")
        String guiaRemision,

        @Size(max = 160, message = "La ubicacion fisica no debe superar 160 caracteres")
        String ubicacionFisica,

        @DecimalMin(value = "0.00", message = "El costo unitario no puede ser negativo")
        BigDecimal costoUnitario,

        @NotNull(message = "La cantidad ingresada es obligatoria")
        @DecimalMin(value = "0.01", message = "La cantidad ingresada debe ser mayor a cero")
        BigDecimal cantidad,

        @DecimalMin(value = "0.00", message = "El stock minimo del lote no puede ser negativo")
        BigDecimal stockMinimo,

        @Size(max = 600, message = "El motivo no debe superar 600 caracteres")
        String motivo,

        @Size(max = 160, message = "El usuario responsable no debe superar 160 caracteres")
        String usuarioResponsable
) {
}