package com.sanfernando.erpclinica.modules.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DciRequestDto(

        @NotBlank(message = "El nombre generico es obligatorio")
        @Size(max = 180, message = "El nombre generico no debe superar 180 caracteres")
        String nombreGenerico,

        @Size(max = 500, message = "La descripcion no debe superar 500 caracteres")
        String descripcion
) {
}