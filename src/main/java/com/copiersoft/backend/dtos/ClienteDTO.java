// Erick_Alquileres
package com.copiersoft.backend.dtos;

import com.copiersoft.backend.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private String codCliente;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String correo;
}
