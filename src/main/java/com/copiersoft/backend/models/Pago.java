package com.copiersoft.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @Column(name = "cod_pago", length = 6, nullable = false)
    private String codPago;

    @Column(name = "tipo_pago", length = 30, nullable = false)
    private String tipoPago;

    @ManyToOne
    @JoinColumn(name = "cod_tipo_comprobante", nullable = false)
    private TipoComprobante tipoComprobante;

    @Column(name = "numero_comprobante", length = 9, nullable = false)
    private String numeroComprobante;

    @Column(name = "forma_pago", length = 30, nullable = false)
    private String formaPago;

    @ManyToOne
    @JoinColumn(name = "cod_medio_pago", nullable = false)
    private MedioPago medioPago;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "sub_total", precision = 11, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "descuento", precision = 11, scale = 2, nullable = false)
    private BigDecimal descuento;

    @Column(name = "igv", precision = 11, scale = 2, nullable = false)
    private BigDecimal igv;

    @Column(name = "importe_total", precision = 11, scale = 2, nullable = false)
    private BigDecimal importeTotal;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @ManyToMany
    @JoinTable(
            name = "pago_alquiler",
            joinColumns = @JoinColumn(name = "cod_pago"),
            inverseJoinColumns = @JoinColumn(name = "cod_alquiler")
    )
    private List<Alquiler> alquileres;
}
