package com.sanfernando.erpclinica.modules.inventory.service;

import com.sanfernando.erpclinica.modules.inventory.dto.CategoriaMedicamentoDto;
import com.sanfernando.erpclinica.modules.inventory.dto.InventoryCatalogsResponseDto;
import com.sanfernando.erpclinica.modules.inventory.dto.UnidadMedidaDto;
import com.sanfernando.erpclinica.modules.inventory.repository.InvCategoriaMedicamentoRepository;
import com.sanfernando.erpclinica.modules.inventory.repository.InvUnidadMedidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryCatalogService {

    private final InvCategoriaMedicamentoRepository categoriaRepository;
    private final InvUnidadMedidaRepository unidadMedidaRepository;

    public InventoryCatalogService(
            InvCategoriaMedicamentoRepository categoriaRepository,
            InvUnidadMedidaRepository unidadMedidaRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @Transactional(readOnly = true)
    public InventoryCatalogsResponseDto getCatalogs() {
        List<CategoriaMedicamentoDto> categorias = categoriaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(item -> new CategoriaMedicamentoDto(
                        item.getId(),
                        item.getNombre(),
                        item.getDescripcion()
                ))
                .toList();

        List<UnidadMedidaDto> unidades = unidadMedidaRepository.findByActivoTrueOrderByCodigoAsc()
                .stream()
                .map(item -> new UnidadMedidaDto(
                        item.getId(),
                        item.getCodigo(),
                        item.getNombre()
                ))
                .toList();

        return new InventoryCatalogsResponseDto(categorias, unidades);
    }
}