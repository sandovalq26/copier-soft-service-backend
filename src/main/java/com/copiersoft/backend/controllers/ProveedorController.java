package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.ProveedorDTO;
import com.copiersoft.backend.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> getAll() {
        return ResponseEntity.ok(proveedorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(proveedorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> create(@RequestBody ProveedorDTO dto) {
        ProveedorDTO newSupplier = proveedorService.create(dto);
        return new ResponseEntity<>(newSupplier, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> update(@PathVariable String id, @RequestBody ProveedorDTO dto) {
        ProveedorDTO updatedSupplier = proveedorService.update(id, dto);
        return ResponseEntity.ok(updatedSupplier);
    }
}
