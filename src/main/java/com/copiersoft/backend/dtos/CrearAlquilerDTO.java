// Erick_Alquileres
package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAlquilerDTO {
    private String codEmpleado;
    private String codCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String periodo;
    private Integer valorPeriodo;
    private BigDecimal precio;
    private String estado;
    private List<String> fotocopiadoras;
}
