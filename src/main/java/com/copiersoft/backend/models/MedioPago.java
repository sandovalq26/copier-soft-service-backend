package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medio_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedioPago {

    @Id
    @Column(name = "cod_medio_pago", length = 6, nullable = false)
    private String codMedioPago;

    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;

}
