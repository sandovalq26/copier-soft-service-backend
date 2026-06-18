// Erick_Alquileres
package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.AlquilerDTO;
import com.copiersoft.backend.dtos.AlquilerDropdownDTO;
import com.copiersoft.backend.dtos.CrearAlquilerDTO;

import java.util.List;

public interface AlquilerService {
    List<AlquilerDropdownDTO> getActiveRentals();
    List<AlquilerDTO> getAll();
    AlquilerDTO getById(String id);
    AlquilerDTO create(CrearAlquilerDTO dto);
    AlquilerDTO update(String id, CrearAlquilerDTO dto);
    void delete(String id);
}
