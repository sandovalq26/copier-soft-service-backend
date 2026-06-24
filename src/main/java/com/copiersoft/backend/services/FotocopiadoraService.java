// Erick_Alquileres
package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.FotocopiadoraDTO;

import java.util.List;

public interface FotocopiadoraService {
    List<FotocopiadoraDTO> getAll();
    FotocopiadoraDTO getById(String codFotocopiadora);
    FotocopiadoraDTO create(FotocopiadoraDTO fotocopiadoraDTO);
    FotocopiadoraDTO update(String codFotocopiadora, FotocopiadoraDTO fotocopiadoraDTO);
}
