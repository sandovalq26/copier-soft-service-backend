package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROVEEDOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @Column(name = "COD_PROVEEDOR", length = 5, nullable = false)
    private String codProveedor;

    @Column(name = "RUC_EMPRESA", length = 11, nullable = false, unique = true)
    private String rucEmpresa;

    @Column(name = "RAZON_SOCIAL", length = 150, nullable = false)
    private String razonSocial;

    @Column(name = "DIRECCION", length = 250)
    private String direccion;

    @Column(name = "CORREO", length = 100)
    private String correo;

}
