// Erick_Alquileres
package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.ClienteDTO;

import java.util.List;

public interface ClienteService {
    List<ClienteDTO> getAll();
    ClienteDTO getById(String codCliente);
    ClienteDTO create(ClienteDTO clienteDTO);
    ClienteDTO update(String codCliente, ClienteDTO clienteDTO);
}
