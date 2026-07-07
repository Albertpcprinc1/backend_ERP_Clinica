package com.sanfernando.erpclinica.modules.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaboratoryRequestDto(

        @NotBlank(message = "El nombre del laboratorio es obligatorio")
        @Size(max = 180, message = "El nombre no debe superar 180 caracteres")
        String nombre,

        @Size(max = 20, message = "El RUC no debe superar 20 caracteres")
        String ruc,

        @Size(max = 100, message = "El pais de origen no debe superar 100 caracteres")
        String paisOrigen,

        @Size(max = 30, message = "El telefono no debe superar 30 caracteres")
        String telefono,

        @Email(message = "El email no tiene un formato valido")
        @Size(max = 160, message = "El email no debe superar 160 caracteres")
        String email,

        @Size(max = 300, message = "La direccion no debe superar 300 caracteres")
        String direccion
) {
}