// Erick_Alquileres
package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.AlquilerDTO;
import com.copiersoft.backend.dtos.AlquilerDropdownDTO;
import com.copiersoft.backend.dtos.CrearAlquilerDTO;
import com.copiersoft.backend.services.AlquilerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class AlquilerController {

    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    @GetMapping("/active")
    public ResponseEntity<List<AlquilerDropdownDTO>> getActiveRentals() {
        return ResponseEntity.ok(service.getActiveRentals());
    }

    @GetMapping
    public ResponseEntity<List<AlquilerDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<AlquilerDTO> create(@RequestBody CrearAlquilerDTO dto) {
        AlquilerDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlquilerDTO> update(@PathVariable String id, @RequestBody CrearAlquilerDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
