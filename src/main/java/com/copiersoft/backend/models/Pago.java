package com.copiersoft.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PAGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @Column(name = "COD_PAGO", length = 6, nullable = false)
    private String codPago;

    @Column(name = "TIPO_PAGO", length = 30, nullable = false)
    private String tipoPago;

    @ManyToOne
    @JoinColumn(name = "COD_TIPO_COMPROBANTE", nullable = false)
    private TipoComprobante tipoComprobante;

    @Column(name = "NUMERO_COMPROBANTE", length = 9, nullable = false)
    private String numeroComprobante;

    @Column(name = "FORMA_PAGO", length = 30, nullable = false)
    private String formaPago;

    @ManyToOne
    @JoinColumn(name = "COD_MEDIO_PAGO", nullable = false)
    private MedioPago medioPago;

    @Column(name = "FECHA_EMISION", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "SUB_TOTAL", precision = 11, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "DESCUENTO", precision = 11, scale = 2, nullable = false)
    private BigDecimal descuento;

    @Column(name = "IGV", precision = 11, scale = 2, nullable = false)
    private BigDecimal igv;

    @Column(name = "IMPORTE_TOTAL", precision = 11, scale = 2, nullable = false)
    private BigDecimal importeTotal;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro;

    @ManyToMany
    @JoinTable(
            name = "PAGO_ALQUILER",
            joinColumns = @JoinColumn(name = "COD_PAGO"),
            inverseJoinColumns = @JoinColumn(name = "COD_ALQUILER")
    )
    private List<Alquiler> alquileres;
}
