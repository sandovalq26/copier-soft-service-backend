package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlmacenDTO {
    private String codAlmacen;
    private String nombre;
    private String direccion;
}
