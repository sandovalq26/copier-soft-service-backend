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
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(name = "cod_cliente", length = 5, nullable = false)
    private String codCliente;

    @Column(name = "tipo_documento", length = 3, nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento", length = 15, nullable = false)
    private String numeroDocumento;

    @Column(name = "nombres", length = 50, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "correo", length = 150)
    private String correo;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

}
