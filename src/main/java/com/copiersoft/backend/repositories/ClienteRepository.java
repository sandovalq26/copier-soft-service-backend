package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    boolean existsByNumeroDocumento(String numeroDocumento);

    Optional<Cliente> findTopByNumeroDocumentoOrderByCodClienteDesc(String numeroDocumento);

}
