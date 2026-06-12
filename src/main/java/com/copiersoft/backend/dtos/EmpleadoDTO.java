package com.copiersoft.backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmpleadoDTO {
    private String codEmpleado;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String correo;
    private String telefono;
}
