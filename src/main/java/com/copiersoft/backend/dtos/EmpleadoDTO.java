package com.copiersoft.backend.dtos;

import com.copiersoft.backend.enums.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {
    private String codEmpleado;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String correo;
    private String telefono;
    
    private String usuario;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;
}
