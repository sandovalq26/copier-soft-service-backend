package com.copiersoft.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "CLIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(name = "COD_CLIENTE", length = 5, nullable = false)
    private String codCliente;

    @Column(name = "TIPO_DOCUMENTO", length = 3, nullable = false)
    private String tipoDocumento;

    @Column(name = "NUMERO_DOCUMENTO", length = 15, nullable = false)
    private String numeroDocumento;

    @Column(name = "NOMBRES", length = 50, nullable = false)
    private String nombres;

    @Column(name = "APELLIDOS", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "DIRECCION", length = 250)
    private String direccion;

    @Column(name = "TELEFONO", length = 30)
    private String telefono;

    @Column(name = "CORREO", length = 150)
    private String correo;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro;

}
