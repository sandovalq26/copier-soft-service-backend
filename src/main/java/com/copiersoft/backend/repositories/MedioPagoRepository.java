package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, String> {
}
