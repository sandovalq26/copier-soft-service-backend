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
@Table(name = "EMPLEADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @Column(name = "COD_EMPLEADO", length = 5, nullable = false)
    private String codEmpleado;

    @Column(name = "TIPO_DOCUMENTO", length = 3, nullable = false)
    private String tipoDocumento;

    @Column(name = "NUMERO_DOCUMENTO", length = 15, nullable = false)
    private String numeroDocumento;

    @Column(name = "NOMBRES", length = 50, nullable = false)
    private String nombres;

    @Column(name = "APELLIDOS", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "CARGO", length = 250, nullable = false)
    private String cargo;

    @Column(name = "TELEFONO", length = 30)
    private String telefono;

    @Column(name = "CORREO", length = 150)
    private String correo;

    @Column(name = "USUARIO", length = 50, nullable = false, unique = true)
    private String usuario;

    @Column(name = "CONTRASENA", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "FECHA_CONTRATACION", nullable = false)
    private LocalDate fechaContratacion;

}
