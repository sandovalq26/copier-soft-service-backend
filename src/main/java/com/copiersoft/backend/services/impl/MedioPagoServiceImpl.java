package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.MedioPagoDTO;
import com.copiersoft.backend.models.MedioPago;
import com.copiersoft.backend.repositories.MedioPagoRepository;
import com.copiersoft.backend.services.MedioPagoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedioPagoServiceImpl implements MedioPagoService {

    private final MedioPagoRepository repository;

    public MedioPagoServiceImpl(MedioPagoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MedioPagoDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MedioPagoDTO mapToDTO(MedioPago model) {
        return new MedioPagoDTO(model.getCodMedioPago(), model.getNombre());
    }
}
