package com.copiersoft.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "FOTOCOPIADORA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fotocopiadora {

    @Id
    @Column(name = "COD_FOTOCOPIADORA", length = 5, nullable = false)
    private String codFotocopiadora;

    @ManyToOne
    @JoinColumn(name = "COD_ALMACEN", nullable = false)
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "COD_PROVEEDOR", nullable = false)
    private Proveedor proveedor;

    @Column(name = "NOMBRE", length = 150, nullable = false)
    private String nombre;

    @Column(name = "MODELO", length = 100, nullable = false)
    private String modelo;

    @Column(name = "SERIE", length = 30, nullable = false)
    private String serie;

    @Column(name = "MARCA", length = 50, nullable = false)
    private String marca;

    @Column(name = "ANIO_FABRICACION", nullable = false)
    private Integer anioFabricacion;

    @Column(name = "ANCHO", nullable = false)
    private Integer ancho;

    @Column(name = "ALTO", nullable = false)
    private Integer alto;

    @Column(name = "FONDO", nullable = false)
    private Integer fondo;

    @Column(name = "ESTADO", length = 30, nullable = false)
    private String estado;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro;
}
