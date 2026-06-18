// Erick_Alquileres
package com.copiersoft.backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AlquilerDTO {
    private String codAlquiler;
    private String codEmpleado;
    private String empleadoNombres;
    private String empleadoApellidos;
    private String codCliente;
    private String clienteTipoDocumento;
    private String clienteNumeroDocumento;
    private String clienteNombres;
    private String clienteApellidos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String periodo;
    private Integer valorPeriodo;
    private BigDecimal precio;
    private String estado;
    private LocalDate fechaRegistro;
    private List<FotocopiadoraDTO> fotocopiadoras;
}
