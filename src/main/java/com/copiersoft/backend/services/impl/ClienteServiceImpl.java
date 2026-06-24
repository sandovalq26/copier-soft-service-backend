// Erick_Alquileres
package com.copiersoft.backend.services.impl;

import com.copiersoft.backend.dtos.ClienteDTO;
import com.copiersoft.backend.enums.TipoDocumento;
import com.copiersoft.backend.models.Cliente;
import com.copiersoft.backend.repositories.ClienteRepository;
import com.copiersoft.backend.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<ClienteDTO> getAll() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO getById(String codCliente) {
        Cliente cliente = clienteRepository.findById(codCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        return mapToDTO(cliente);
    }

    @Override
    public ClienteDTO create(ClienteDTO dto) {
        validarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());

        if (clienteRepository.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ya se encuentra registrado.");
        }

        Cliente cliente = new Cliente();
        cliente.setCodCliente(""); // El trigger generará el verdadero ID

        // Mapeo manual de DTO a Entidad
        cliente.setTipoDocumento(dto.getTipoDocumento().name());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());
        cliente.setFechaRegistro(LocalDate.now());

        clienteRepository.save(cliente);

        // Recuperar el cliente con el ID generado por el trigger
        Cliente savedCliente = clienteRepository.findTopByNumeroDocumentoOrderByCodClienteDesc(dto.getNumeroDocumento())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar el cliente creado"));

        return mapToDTO(savedCliente);
    }

    @Override
    public ClienteDTO update(String codCliente, ClienteDTO dto) {
        validarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());

        Cliente cliente = clienteRepository.findById(codCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        // Validar duplicidad de documento solo si está cambiando de documento
        if (!cliente.getNumeroDocumento().equals(dto.getNumeroDocumento()) &&
                clienteRepository.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento ingresado ya está registrado a otro cliente.");
        }

        cliente.setTipoDocumento(dto.getTipoDocumento().name());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());

        Cliente updatedCliente = clienteRepository.save(cliente);
        return mapToDTO(updatedCliente);
    }

    private void validarDocumento(com.copiersoft.backend.enums.TipoDocumento tipo, String numeroDocumento) {
        if (numeroDocumento == null || !numeroDocumento.matches("^[0-9]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de documento debe contener solo dígitos.");
        }

        switch (tipo) {
            case DNI:
                if (numeroDocumento.length() != 8) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DNI debe tener 8 dígitos.");
                }
                break;
            case RUC:
                if (numeroDocumento.length() != 11) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC debe tener 11 dígitos.");
                }
                break;
            case CEX:
                if (numeroDocumento.length() < 8 || numeroDocumento.length() > 15) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El CEX debe tener entre 8 y 15 dígitos.");
                }
                break;
        }
    }

    private ClienteDTO mapToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setCodCliente(cliente.getCodCliente());
        try {
            dto.setTipoDocumento(TipoDocumento.valueOf(cliente.getTipoDocumento()));
        } catch (IllegalArgumentException | NullPointerException e) {
            dto.setTipoDocumento(null);
        }
        dto.setNumeroDocumento(cliente.getNumeroDocumento());
        dto.setNombres(cliente.getNombres());
        dto.setApellidos(cliente.getApellidos());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setCorreo(cliente.getCorreo());
        return dto;
    }
}
