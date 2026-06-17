package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoComprobanteRepository extends JpaRepository<TipoComprobante, String> {
}
