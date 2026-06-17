package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.AlquilerDropdownDTO;
import com.copiersoft.backend.models.Alquiler;
import com.copiersoft.backend.repositories.AlquilerRepository;
import com.copiersoft.backend.services.AlquilerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    private final AlquilerRepository repository;

    public AlquilerServiceImpl(AlquilerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AlquilerDropdownDTO> getActiveRentals() {
        return repository.findByEstado("EN EJECUCION").stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AlquilerDropdownDTO mapToDTO(Alquiler model) {
        String clienteNombre = model.getCliente().getNombres() + " " + model.getCliente().getApellidos();
        return new AlquilerDropdownDTO(model.getCodAlquiler(), clienteNombre, model.getPrecio());
    }
}
