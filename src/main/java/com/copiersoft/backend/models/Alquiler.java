package com.copiersoft.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "alquiler")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {

    @Id
    @Column(name = "cod_alquiler", length = 6, nullable = false)
    private String codAlquiler;

    @ManyToOne
    @JoinColumn(name = "cod_empleado", nullable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "cod_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "periodo", length = 15, nullable = false)
    private String periodo;

    @Column(name = "valor_periodo", nullable = false)
    private Integer valorPeriodo;

    @Column(name = "precio", precision = 11, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @ManyToMany
    @JoinTable(
            name = "alquiler_fotocopiadora",
            joinColumns = @JoinColumn(name = "cod_alquiler"),
            inverseJoinColumns = @JoinColumn(name = "cod_fotocopiadora")
    )
    private List<Fotocopiadora> fotocopiadoras;
}
