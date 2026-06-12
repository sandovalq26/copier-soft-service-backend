package com.copiersoft.backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class AlquilerRecienteDTO {
    private String codAlquiler;
    private String clienteNombres;
    private String clienteApellidos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private BigDecimal precio;
}
