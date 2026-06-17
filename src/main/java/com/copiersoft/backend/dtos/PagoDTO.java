package com.copiersoft.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private String codPago;
    private String codAlquiler;

    // Campos de solo lectura para listar
    private String clienteDocumento;
    private String clienteNombres;
    private String comprobanteNombre;
    private String medioPagoNombre;

    // Campos para creación
    private String tipoPago;
    private String codTipoComprobante;
    private String numeroComprobante;
    private String formaPago;
    private String codMedioPago;
    
    private LocalDate fechaEmision;
    private BigDecimal subTotal;
    private BigDecimal descuento;
    private BigDecimal igv;
    private BigDecimal importeTotal;
    
    private boolean pagoCompleto; // Para saber si finaliza el alquiler
}
