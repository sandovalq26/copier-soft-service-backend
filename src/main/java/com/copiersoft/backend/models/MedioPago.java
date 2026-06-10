package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDIO_PAGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedioPago {

    @Id
    @Column(name = "COD_MEDIO_PAGO", length = 6, nullable = false)
    private String codMedioPago;

    @Column(name = "NOMBRE", length = 100, nullable = false, unique = true)
    private String nombre;

}
