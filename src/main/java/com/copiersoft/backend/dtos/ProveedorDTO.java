package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {
    private String codProveedor;
    private String rucEmpresa;
    private String razonSocial;
    private String direccion;
    private String correo;
}
