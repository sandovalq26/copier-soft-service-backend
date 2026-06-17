package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedioPagoDTO {
    private String codMedioPago;
    private String nombre;
}
