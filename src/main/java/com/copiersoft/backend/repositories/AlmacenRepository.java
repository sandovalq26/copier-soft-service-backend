package com.copiersoft.backend.repositories;

import com.copiersoft.backend.models.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, String> {
}
