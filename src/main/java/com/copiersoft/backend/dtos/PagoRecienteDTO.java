package com.copiersoft.backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PagoRecienteDTO {
    private String codPago;
    private String clienteNombres;
    private String clienteApellidos;
    private BigDecimal importeTotal;
    private String medioPagoNombre;
}
