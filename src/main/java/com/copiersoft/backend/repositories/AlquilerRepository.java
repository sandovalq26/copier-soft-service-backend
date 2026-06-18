// Erick_Alquileres
package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, String> {
    List<Alquiler> findTop5ByOrderByFechaRegistroDesc();
    List<Alquiler> findByEstado(String estado);
    Optional<Alquiler> findTopByOrderByCodAlquilerDesc();
}
