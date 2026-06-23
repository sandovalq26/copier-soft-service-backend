package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {
    Optional<Empleado> findByUsuarioAndContrasena(String usuario, String contrasena);
    
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    boolean existsByUsuario(String usuario);
    
    Optional<Empleado> findTopByNumeroDocumentoOrderByCodEmpleadoDesc(String numeroDocumento);
}
