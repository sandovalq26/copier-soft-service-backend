package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.ProveedorDTO;
import java.util.List;

public interface ProveedorService {
    List<ProveedorDTO> getAll();
    ProveedorDTO getById(String id);
    ProveedorDTO create(ProveedorDTO dto);
    ProveedorDTO update(String id, ProveedorDTO dto);
}
