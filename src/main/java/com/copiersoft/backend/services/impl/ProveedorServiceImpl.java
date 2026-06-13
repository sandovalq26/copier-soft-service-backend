package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.ProveedorDTO;
import com.copiersoft.backend.models.Proveedor;
import com.copiersoft.backend.repositories.ProveedorRepository;
import com.copiersoft.backend.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<ProveedorDTO> getAll() {
        return proveedorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProveedorDTO getById(String id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found with ID: " + id));
        return mapToDTO(proveedor);
    }

    @Override
    public ProveedorDTO create(ProveedorDTO dto) {
        if (proveedorRepository.existsByRucEmpresa(dto.getRucEmpresa())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier RUC is already registered.");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setCodProveedor("");
        proveedor.setRucEmpresa(dto.getRucEmpresa());
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setCorreo(dto.getCorreo());

        Proveedor savedSupplier = proveedorRepository.save(proveedor);
        return mapToDTO(savedSupplier);
    }

    @Override
    public ProveedorDTO update(String id, ProveedorDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found with ID: " + id));

        if (proveedorRepository.existsByRucEmpresaAndCodProveedorNot(dto.getRucEmpresa(), id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided RUC already belongs to another supplier.");
        }

        proveedor.setRucEmpresa(dto.getRucEmpresa());
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setCorreo(dto.getCorreo());

        Proveedor savedSupplier = proveedorRepository.save(proveedor);
        return mapToDTO(savedSupplier);
    }

    private ProveedorDTO mapToDTO(Proveedor proveedor) {
        return new ProveedorDTO(
                proveedor.getCodProveedor(),
                proveedor.getRucEmpresa(),
                proveedor.getRazonSocial(),
                proveedor.getDireccion(),
                proveedor.getCorreo()
        );
    }
}
