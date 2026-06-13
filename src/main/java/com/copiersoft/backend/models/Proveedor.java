package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @Column(name = "cod_proveedor", length = 5, nullable = false)
    private String codProveedor;

    @Column(name = "ruc_empresa", length = 11, nullable = false, unique = true)
    private String rucEmpresa;

    @Column(name = "razon_social", length = 150, nullable = false)
    private String razonSocial;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "correo", length = 100)
    private String correo;

}
