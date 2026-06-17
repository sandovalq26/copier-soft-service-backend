package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerDropdownDTO {
    private String codAlquiler;
    private String clienteNombres;
    private BigDecimal precio;
}
