package com.copiersoft.backend.services;

import com.copiersoft.backend.dtos.EmpleadoDTO;

import java.util.List;

public interface EmpleadoService {
    List<EmpleadoDTO> getAll();
    EmpleadoDTO getById(String codEmpleado);
    EmpleadoDTO create(EmpleadoDTO empleadoDTO);
    EmpleadoDTO update(String codEmpleado, EmpleadoDTO empleadoDTO);
}
