package com.copiersoft.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ALQUILER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {

    @Id
    @Column(name = "COD_ALQUILER", length = 6, nullable = false)
    private String codAlquiler;

    @ManyToOne
    @JoinColumn(name = "COD_EMPLEADO", nullable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "COD_CLIENTE", nullable = false)
    private Cliente cliente;

    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FECHA_FIN", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "PERIODO", length = 15, nullable = false)
    private String periodo;

    @Column(name = "VALOR_PERIODO", nullable = false)
    private Integer valorPeriodo;

    @Column(name = "PRECIO", precision = 11, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "ESTADO", length = 30, nullable = false)
    private String estado;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro;

    @ManyToMany
    @JoinTable(
            name = "ALQUILER_FOTOCOPIADORA",
            joinColumns = @JoinColumn(name = "COD_ALQUILER"),
            inverseJoinColumns = @JoinColumn(name = "COD_FOTOCOPIADORA")
    )
    private List<Fotocopiadora> fotocopiadoras;
}
