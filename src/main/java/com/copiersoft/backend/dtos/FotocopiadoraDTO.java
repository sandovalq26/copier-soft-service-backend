// Erick_Alquileres
package com.copiersoft.backend.dtos;

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
    private String nombre;
    private String marca;
    private String modelo;
    private String serie;
    private Integer ancho;
    private Integer alto;
    private Integer fondo;
}
