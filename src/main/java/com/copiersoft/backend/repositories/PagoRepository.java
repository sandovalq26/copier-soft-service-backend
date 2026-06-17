package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
    List<Pago> findTop5ByOrderByFechaRegistroDesc();
    Optional<Pago> findTopByNumeroComprobanteOrderByCodPagoDesc(String numeroComprobante);
}
