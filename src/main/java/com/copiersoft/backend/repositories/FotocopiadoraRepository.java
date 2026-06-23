package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Fotocopiadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FotocopiadoraRepository extends JpaRepository<Fotocopiadora, String> {
    boolean existsBySerie(String serie);
    Optional<Fotocopiadora> findTopBySerieOrderByCodFotocopiadoraDesc(String serie);
}
