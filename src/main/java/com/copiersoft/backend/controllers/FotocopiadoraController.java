// Erick_Alquileres
package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.FotocopiadoraDTO;
import com.copiersoft.backend.services.FotocopiadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photocopiers")
@CrossOrigin(origins = "*")
public class FotocopiadoraController {

    @Autowired
    private FotocopiadoraService fotocopiadoraService;

    @GetMapping
    public ResponseEntity<List<FotocopiadoraDTO>> getAll() {
        return ResponseEntity.ok(fotocopiadoraService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotocopiadoraDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(fotocopiadoraService.getById(id));
    }

    @PostMapping
    public ResponseEntity<FotocopiadoraDTO> create(@RequestBody FotocopiadoraDTO fotocopiadoraDTO) {
        FotocopiadoraDTO created = fotocopiadoraService.create(fotocopiadoraDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotocopiadoraDTO> update(@PathVariable String id, @RequestBody FotocopiadoraDTO fotocopiadoraDTO) {
        return ResponseEntity.ok(fotocopiadoraService.update(id, fotocopiadoraDTO));
    }
}
