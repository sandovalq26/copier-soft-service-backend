package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ALMACEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {

    @Id
    @Column(name = "COD_ALMACEN", length = 5, nullable = false)
    private String codAlmacen;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "DIRECCION", length = 250, nullable = false)
    private String direccion;

}
