package com.sanfernando.erpclinica.modules.inventory.dto;

import java.util.List;

public record InventoryCatalogsResponseDto(
        List<CategoriaMedicamentoDto> categorias,
        List<UnidadMedidaDto> unidades
) {
}