package com.copiersoft.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "fotocopiadora")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fotocopiadora {

    @Id
    @Column(name = "cod_fotocopiadora", length = 5, nullable = false)
    private String codFotocopiadora;

    @ManyToOne
    @JoinColumn(name = "cod_almacen", nullable = false)
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "cod_proveedor", nullable = false)
    private Proveedor proveedor;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "modelo", length = 100, nullable = false)
    private String modelo;

    @Column(name = "serie", length = 30, nullable = false)
    private String serie;

    @Column(name = "marca", length = 50, nullable = false)
    private String marca;

    @Column(name = "anio_fabricacion", nullable = false)
    private Integer anioFabricacion;

    @Column(name = "ancho", nullable = false)
    private Integer ancho;

    @Column(name = "alto", nullable = false)
    private Integer alto;

    @Column(name = "fondo", nullable = false)
    private Integer fondo;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;
}
