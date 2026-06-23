// Erick_Alquileres
package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.FotocopiadoraDTO;
import com.copiersoft.backend.models.Fotocopiadora;
import com.copiersoft.backend.repositories.FotocopiadoraRepository;
import com.copiersoft.backend.services.FotocopiadoraService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FotocopiadoraServiceImpl implements FotocopiadoraService {

    private final FotocopiadoraRepository repository;

    public FotocopiadoraServiceImpl(FotocopiadoraRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FotocopiadoraDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FotocopiadoraDTO mapToDTO(Fotocopiadora model) {
        return FotocopiadoraDTO.builder()
                .codFotocopiadora(model.getCodFotocopiadora())
                .nombre(model.getNombre())
                .marca(model.getMarca())
                .modelo(model.getModelo())
                .serie(model.getSerie())
                .ancho(model.getAncho())
                .alto(model.getAlto())
                .fondo(model.getFondo())
                .build();
    }
}
