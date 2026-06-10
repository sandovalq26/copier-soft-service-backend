package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TIPO_COMPROBANTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoComprobante {

    @Id
    @Column(name = "COD_TIPO_COMPROBANTE", length = 2, nullable = false)
    private String codTipoComprobante;

    @Column(name = "NOMBRE", length = 100, nullable = false)
    private String nombre;

}
