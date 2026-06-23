// Erick_Alquileres
package com.copiersoft.backend.controllers;

import com.copiersoft.backend.dtos.FotocopiadoraDTO;
import com.copiersoft.backend.services.FotocopiadoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/photocopiers")
@CrossOrigin(origins = "*")
public class FotocopiadoraController {

    private final FotocopiadoraService service;

    public FotocopiadoraController(FotocopiadoraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FotocopiadoraDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
