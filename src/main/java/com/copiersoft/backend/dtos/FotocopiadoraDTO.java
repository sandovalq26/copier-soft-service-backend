package com.copiersoft.backend.dtos;

import com.copiersoft.backend.enums.EstadoFotocopiadora;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotocopiadoraDTO {
    private String codFotocopiadora;
    private String codAlmacen;
    private String codProveedor;
    private String nombre;
    private String marca;
    private String modelo;
    private String serie;
    private Integer anioFabricacion;
    private Integer ancho;
    private Integer alto;
    private Integer fondo;
    private EstadoFotocopiadora estado;
}
