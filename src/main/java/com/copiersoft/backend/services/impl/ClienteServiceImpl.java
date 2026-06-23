// Erick_Alquileres
package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.ClienteDTO;
import com.copiersoft.backend.models.Cliente;
import com.copiersoft.backend.repositories.ClienteRepository;
import com.copiersoft.backend.services.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ClienteDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ClienteDTO mapToDTO(Cliente model) {
        return new ClienteDTO(
                model.getCodCliente(),
                model.getTipoDocumento(),
                model.getNumeroDocumento(),
                model.getNombres(),
                model.getApellidos(),
                model.getDireccion(),
                model.getTelefono(),
                model.getCorreo()
        );
    }
}
