package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoComprobanteDTO {
    private String codTipoComprobante;
    private String nombre;
}
