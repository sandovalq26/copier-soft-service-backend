package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, String> {
    boolean existsByRucEmpresa(String rucEmpresa);
    boolean existsByRucEmpresaAndCodProveedorNot(String rucEmpresa, String codProveedor);
    Optional<Proveedor> findByRucEmpresa(String rucEmpresa);
}
