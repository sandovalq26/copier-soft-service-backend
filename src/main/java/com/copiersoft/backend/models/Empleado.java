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
@Table(name = "empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @Column(name = "cod_empleado", length = 5, nullable = false)
    private String codEmpleado;

    @Column(name = "tipo_documento", length = 3, nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento", length = 15, nullable = false)
    private String numeroDocumento;

    @Column(name = "nombres", length = 50, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "cargo", length = 250, nullable = false)
    private String cargo;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "correo", length = 150)
    private String correo;

    @Column(name = "usuario", length = 50, nullable = false, unique = true)
    private String usuario;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

}
