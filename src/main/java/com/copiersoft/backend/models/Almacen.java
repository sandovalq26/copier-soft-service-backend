package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "almacen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {

    @Id
    @Column(name = "cod_almacen", length = 5, nullable = false)
    private String codAlmacen;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 250, nullable = false)
    private String direccion;

}
