package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_comprobante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoComprobante {

    @Id
    @Column(name = "cod_tipo_comprobante", length = 2, nullable = false)
    private String codTipoComprobante;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

}
