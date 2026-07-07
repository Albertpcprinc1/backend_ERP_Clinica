package com.sanfernando.erpclinica.modules.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record StockOutputRequestDto(

        @NotNull(message = "El medicamento es obligatorio")
        Long medicamentoId,

        @NotNull(message = "La cantidad solicitada es obligatoria")
        @DecimalMin(value = "0.01", message = "La cantidad solicitada debe ser mayor a cero")
        BigDecimal cantidad,

        @Size(max = 60, message = "El tipo de movimiento no debe superar 60 caracteres")
        String tipoMovimiento,

        @Size(max = 80, message = "El tipo de referencia no debe superar 80 caracteres")
        String referenciaTipo,

        Long referenciaId,

        @Size(max = 600, message = "El motivo no debe superar 600 caracteres")
        String motivo,

        @Size(max = 160, message = "El usuario responsable no debe superar 160 caracteres")
        String usuarioResponsable
) {
}