package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, String> {
    List<Alquiler> findTop5ByOrderByFechaRegistroDesc();
}
