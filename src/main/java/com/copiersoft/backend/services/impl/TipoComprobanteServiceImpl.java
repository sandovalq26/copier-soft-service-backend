package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.TipoComprobanteDTO;
import com.copiersoft.backend.models.TipoComprobante;
import com.copiersoft.backend.repositories.TipoComprobanteRepository;
import com.copiersoft.backend.services.TipoComprobanteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoComprobanteServiceImpl implements TipoComprobanteService {

    private final TipoComprobanteRepository repository;

    public TipoComprobanteServiceImpl(TipoComprobanteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoComprobanteDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TipoComprobanteDTO mapToDTO(TipoComprobante model) {
        return new TipoComprobanteDTO(model.getCodTipoComprobante(), model.getNombre());
    }
}
